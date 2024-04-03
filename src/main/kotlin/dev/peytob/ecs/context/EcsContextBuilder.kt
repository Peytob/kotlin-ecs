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
    var entityManager: EntityManager? = null,
    var componentManager: ComponentManager? = null,
    var systemManager: SystemManager? = null,
    var eventManager: EventManager? = null,

    var ecsEntityIdGenerator: EcsEntityIdGenerator? = RandomStringEcsEntityIdGenerator()
) {

    private val entityInitializers = mutableListOf<EntityInitializer>()

    private val systems = mutableListOf<EcsSystem>()

    fun appendEntity(customId: String? = null, initializer: (EcsEntity, EcsContext) -> Unit): EcsContextBuilder {
        val entityInitializer = EntityInitializer(customId, initializer)
        return this
    }

    fun appendSystem(system: EcsSystem): EcsContextBuilder {
        systems.add(system)
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

        systems.forEach(activeSystemManager::appendSystem)

        entityInitializers.forEach {
            val ecsEntity = if (it.customId != null) context.createEntity(it.customId) else context.createEntity()
            it.initializer(ecsEntity, context)
        }

        return context
    }

    private data class EntityInitializer(
        val customId: String?,
        val initializer: (EcsEntity, EcsContext) -> Unit
    )
}
