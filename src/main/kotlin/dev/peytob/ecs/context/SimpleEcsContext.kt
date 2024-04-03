package dev.peytob.ecs.context

import dev.peytob.ecs.component.EcsComponent
import dev.peytob.ecs.component.SingletonEcsComponent
import dev.peytob.ecs.component.manager.ComponentManager
import dev.peytob.ecs.context.generator.EcsEntityIdGenerator
import dev.peytob.ecs.entity.EcsEntity
import dev.peytob.ecs.entity.GenericEcsEntity
import dev.peytob.ecs.entity.manager.EntityManager
import dev.peytob.ecs.event.EcsEvent
import dev.peytob.ecs.event.EventManager
import dev.peytob.ecs.exception.EcsEntityAlreadyExistsException
import dev.peytob.ecs.system.EcsSystem
import dev.peytob.ecs.system.manager.SystemManager

internal class SimpleEcsContext(
    private val entityManager: EntityManager,
    private val componentManager: ComponentManager,
    private val systemManager: SystemManager,
    private val eventManager: EventManager,

    private val ecsEntityIdGenerator: EcsEntityIdGenerator
) : EcsContext {

    // TODO Mage separated module
    private val componentToEntityMap: MutableMap<EcsComponent, EcsEntity> = mutableMapOf()

    override fun createEntity(): EcsEntity {
        val entityId = ecsEntityIdGenerator.generateNextId()
        return createEntity(entityId)
    }

    override fun createEntity(id: String): EcsEntity {
        val existsEntity = entityManager.getEntityById(id)

        if (existsEntity != null) {
            throw EcsEntityAlreadyExistsException("Entity with id $id already exists in the context", existsEntity)
        }

        val entity = GenericEcsEntity(id)
        val contextEntity = ContextEcsEntity(entity, this)
        entityManager.appendEntity(contextEntity)
        return contextEntity
    }

    override fun removeEntity(entity: EcsEntity) {
        if (entityManager.getEntityById(entity.id) != entity) {
            return
        }

        entity.getComponents()
            .forEach { componentManager.removeComponent(it) }

        entityManager.removeEntity(entity)
    }

    override fun getEntityById(entityId: String): EcsEntity? {
        return entityManager.getEntityById(entityId)
    }

    override fun getAllEntities(): Collection<EcsEntity> {
        return entityManager.getAllEntities()
    }

    override fun getComponentEntity(component: EcsComponent): EcsEntity? {
        return componentToEntityMap[component]
    }

    override fun <T : EcsComponent> getComponentsByType(componentType: Class<T>): Collection<T> {
        return componentManager.getComponentsByType(componentType)
    }

    override fun <T : SingletonEcsComponent> getSingletonComponentByType(componentType: Class<T>): T? {
        return componentManager.getSingletonComponentByType(componentType)
    }

    override fun getComponentTypes(): Collection<Class<out EcsComponent>> {
        return componentManager.getComponentTypes()
    }

    override fun getSystems(): Collection<EcsSystem> {
        return systemManager.getAllSystems()
    }

    override fun appendEvent(ecsEvent: EcsEvent) {
        eventManager.appendEvent(ecsEvent)
    }

    override fun <T : EcsEvent> getEventsByType(ecsEvent: Class<T>): Collection<T> {
        return eventManager.getEventsByType(ecsEvent)
    }

    override fun removeAllEventsByType(eventType: Class<out EcsEvent>) {
        eventManager.removeAllEventsByType(eventType)
    }

    override fun getEventTypes(): Collection<Class<out EcsEvent>> {
        return eventManager.getEventTypes()
    }

    override fun clearEvents() {
        eventManager.clear()
    }

    override fun removeEvent(ecsEvent: EcsEvent) {
        eventManager.removeEvent(ecsEvent)
    }

    private class ContextEcsEntity(
        private val entity: EcsEntity,
        private val ecsContext: SimpleEcsContext
    ) : EcsEntity {

        override val id: String = entity.id

        override fun getComponents(): Collection<EcsComponent> = entity.getComponents()

        override fun getComponentTypes(): Collection<Class<out EcsComponent>> = entity.getComponentTypes()

        override fun <T : EcsComponent> getComponent(componentType: Class<T>): T? = entity.getComponent(componentType)

        override fun <T : EcsComponent> removeComponent(componentType: Class<T>): T? {
            val component = entity.removeComponent(componentType)

            if (component != null) {
                ecsContext.componentManager.removeComponent(component)
                ecsContext.componentToEntityMap.remove(component)
            }

            return component
        }

        override fun <T : EcsComponent> containsComponent(componentType: Class<T>): Boolean = entity.containsComponent(componentType)

        override fun appendComponent(component: EcsComponent) {
            entity.appendComponent(component)
            ecsContext.componentManager.appendComponent(component)
            ecsContext.componentToEntityMap[component] = this
        }

        override fun isEmpty(): Boolean = entity.isEmpty()

        override fun isAlive(): Boolean = ecsContext.entityManager.getEntityById(id) != null
    }
}
