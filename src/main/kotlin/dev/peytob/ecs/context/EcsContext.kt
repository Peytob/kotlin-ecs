package dev.peytob.ecs.context

import dev.peytob.ecs.component.EcsComponent
import dev.peytob.ecs.component.SingletonEcsComponent
import dev.peytob.ecs.entity.EcsEntity
import dev.peytob.ecs.event.EcsEvent
import dev.peytob.ecs.system.EcsSystem

interface EcsContext {


    /**
     * Creates new entity with new random ID
     */
    fun createEntity(): EcsEntity

    /**
     * Creates new entity with given ID. Throws EcsEntityAlreadyExistsException, if entity with this ID is already exists.
     */
    fun createEntity(id: String): EcsEntity

    /**
     * Removes given entity.
     */
    fun removeEntity(entity: EcsEntity)

    /**
     * Returns an entity with the given id if it exists.
     */
    fun getEntityById(entityId: String): EcsEntity?

    /**
     * Returns all context entities.
     */
    fun getAllEntities(): Collection<EcsEntity>

    /**
     * Returns entity, that bound with given component. If component is not managed by context - returns null.
     */
    fun getComponentEntity(component: EcsComponent): EcsEntity?

    /**
     * Returns all context components with given type.
     */
    fun <T : EcsComponent> getComponentsByType(componentType: Class<T>): Collection<T>

    /**
     * Returns singleton component, if it exists.
     */
    fun <T : SingletonEcsComponent> getSingletonComponentByType(componentType: Class<T>): T?

    /**
     * Returns all types of components in the context.
     */
    fun getComponentTypes(): Collection<Class<out EcsComponent>>

    /**
     * Returns all systems in the context. The order of systems corresponds to the order of their execution.
     */
    fun getSystems(): Collection<EcsSystem>

    /**
     * Adds event to the context. One event can be registered more, that one time.
     */
    fun appendEvent(ecsEvent: EcsEvent)

    /**
     * Returns all events components with given type. The order of events corresponds to the order in which they were appended.
     */
    fun <T : EcsEvent> getEventsByType(ecsEvent: Class<T>): Collection<T>

    /**
     * Returns all events with given type.
     */
    fun removeAllEventsByType(eventType: Class<out EcsEvent>)

    /**
     * Returns all types of events in the context.
     */
    fun getEventTypes(): Collection<Class<out EcsEvent>>

    /**
     * Removes all events in the context.
     */
    fun clearEvents()

    /**
     * Removes given events from context.
     */
    fun removeEvent(ecsEvent: EcsEvent)

}
