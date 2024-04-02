package dev.peytob.ecs.entity

import dev.peytob.ecs.component.EcsComponent
import dev.peytob.ecs.exception.EcsComponentAlreadyExistsException

internal class GenericEcsEntity(
    override val id: String
) : EcsEntity {

    private val components: MutableMap<Class<out EcsComponent>, EcsComponent> = mutableMapOf()

    override fun getComponents(): Collection<EcsComponent> {
        return components.values
    }

    override fun getComponentTypes(): Collection<Class<out EcsComponent>> {
        return components.keys
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : EcsComponent> getComponent(componentType: Class<T>): T? {
        return if (!containsComponent(componentType)) null else components[componentType] as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : EcsComponent> removeComponent(componentType: Class<T>): T? {
        return if (!containsComponent(componentType)) null else components.remove(componentType) as T
    }

    override fun <T : EcsComponent> containsComponent(componentType: Class<T>): Boolean {
        return components.containsKey(componentType)
    }

    override fun appendComponent(component: EcsComponent) {
        if (containsComponent(component.javaClass)) {
            throw EcsComponentAlreadyExistsException("Component already exists in this entity", component)
        }

        components[component.javaClass] = component
    }

    override fun isEmpty(): Boolean {
        return components.isEmpty()
    }

    override fun isAlive(): Boolean {
        // Generic entity is always alive
        return true
    }
}