package dev.peytob.ecs.context

import dev.peytob.ecs.component.manager.ComponentManager
import dev.peytob.ecs.component.manager.SimpleComponentManager
import dev.peytob.ecs.context.generator.EcsEntityIdGenerator
import dev.peytob.ecs.context.generator.RandomStringEcsEntityIdGenerator
import dev.peytob.ecs.entity.EcsEntity
import dev.peytob.ecs.entity.manager.EntityManager
import dev.peytob.ecs.entity.manager.SimpleEntityManager
import dev.peytob.ecs.event.EventManager
import dev.peytob.ecs.event.SimpleEventManager
import dev.peytob.ecs.system.EcsSystem
import dev.peytob.ecs.system.manager.SimpleSystemManager
import dev.peytob.ecs.system.manager.SystemManager

class EcsContextBuilder(
    private val entityManager: EntityManager? = null,
    private val componentManager: ComponentManager? = null,
    private val systemManager: SystemManager? = null,
    private val eventManager: EventManager? = null,

    private val ecsEntityIdGenerator: EcsEntityIdGenerator? = RandomStringEcsEntityIdGenerator()
) {

    companion object {
        const val ORDERLESS_SYSTEMS_START_INDEX = Int.MAX_VALUE / 2
        const val ORDERLESS_SYSTEMS_STEP = 128
    }

    private val entityInitializers = mutableListOf<EntityInitializer>()

    private val systems = mutableListOf<OrderedSystem>()

    fun appendEntity(customId: String? = null, initializer: (EcsEntity, EcsContext) -> Unit): EcsContextBuilder {
        val entityInitializer = EntityInitializer(customId, initializer)
        entityInitializers.add(entityInitializer)
        return this
    }

    fun appendSystem(system: EcsSystem): EcsContextBuilder {
        // Can be optimized
        val lastSystemOrder = systems
            .lastOrNull()
            ?.order
            ?.plus(ORDERLESS_SYSTEMS_STEP)
            ?: ORDERLESS_SYSTEMS_START_INDEX

        systems.add(OrderedSystem(system, lastSystemOrder))

        return this
    }

    fun appendSystem(system: EcsSystem, order: Int): EcsContextBuilder {
        val targetSystemIndex = systems.indexOfLast { it.order < order }
        systems.add(targetSystemIndex + 1, OrderedSystem(system, order))
        return this
    }

    fun appendSystemAfter(system: EcsSystem, ecsSystem: EcsSystem): EcsContextBuilder {
        return appendSystemAfter(system, ecsSystem.javaClass)
    }

    fun appendSystemAfter(system: EcsSystem, ecsSystemType: Class<out EcsSystem>): EcsContextBuilder {
        val targetSystemIndex = systems.indexOfLast { ecsSystemType.isInstance(it.system) }

        if (targetSystemIndex < 0) {
            throw systemNotFound(ecsSystemType)
        }

        val appendedSystemOrder = systems[targetSystemIndex].order + 1
        systems.add(targetSystemIndex + 1, OrderedSystem(system, appendedSystemOrder))

        return this
    }

    fun appendSystemBefore(system: EcsSystem, ecsSystem: EcsSystem): EcsContextBuilder {
        return appendSystemBefore(system, ecsSystem.javaClass)
    }

    fun appendSystemBefore(system: EcsSystem, ecsSystemType: Class<out EcsSystem>): EcsContextBuilder {
        val targetSystemIndex = systems.indexOfLast { ecsSystemType.isInstance(it.system) }

        if (targetSystemIndex < 0) {
            throw systemNotFound(ecsSystemType)
        }

        val appendedSystemOrder = systems[targetSystemIndex].order - 1

        val appendingSystemIndex = if (targetSystemIndex == 0) 0 else targetSystemIndex - 1
        systems.add(appendingSystemIndex, OrderedSystem(system, appendedSystemOrder))

        return this
    }

    fun build(): EcsContext {
        val activeSystemManager = this.systemManager ?: SimpleSystemManager()

        val context = SimpleEcsContext(
            entityManager = this.entityManager ?: SimpleEntityManager(),
            componentManager = this.componentManager ?: SimpleComponentManager(),
            systemManager = activeSystemManager,
            eventManager = this.eventManager ?: SimpleEventManager(),
            ecsEntityIdGenerator = this.ecsEntityIdGenerator ?: RandomStringEcsEntityIdGenerator()
        )

        systems
            .sortedBy(OrderedSystem::order)
            .map(OrderedSystem::system)
            .forEach(activeSystemManager::appendSystem)

        entityInitializers.forEach {
            val ecsEntity = if (it.customId != null) context.createEntity(it.customId) else context.createEntity()
            it.initializer(ecsEntity, context)
        }

        return context
    }

    private fun systemNotFound(ecsSystemType: Class<out EcsSystem>): Exception {
        return IllegalAccessException("System with type ${ecsSystemType.simpleName} not found in builder context")
    }

    private data class EntityInitializer(
        val customId: String?,
        val initializer: (EcsEntity, EcsContext) -> Unit
    )

    private data class OrderedSystem(
        val system: EcsSystem,
        val order: Int
    )
}
