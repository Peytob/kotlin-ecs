package dev.peytob.ecs.component.manager

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import dev.peytob.ecs.component.EcsComponent
import dev.peytob.ecs.component.SingletonEcsComponent
import dev.peytob.ecs.exception.EcsComponentAlreadyExistsException

class SimpleComponentManager : ComponentManager {

    private val components: Multimap<Class<out EcsComponent>, EcsComponent> = HashMultimap.create()

    override fun appendComponent(ecsComponent: EcsComponent) {
        if (components.containsEntry(ecsComponent.javaClass, ecsComponent)) {
            throw EcsComponentAlreadyExistsException("Component already exists", ecsComponent)
        }

        if (ecsComponent is SingletonEcsComponent && components.containsKey(ecsComponent.javaClass)) {
            throw EcsComponentAlreadyExistsException("Singleton component with same type is already exists", ecsComponent)
        }

        components.put(ecsComponent.javaClass, ecsComponent)
    }

    override fun removeComponent(ecsComponent: EcsComponent) {
        components.remove(ecsComponent.javaClass, ecsComponent)
    }

    override fun getComponentTypes(): Collection<Class<out EcsComponent>> {
        return components.keySet()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : EcsComponent> getComponentsByType(componentType: Class<T>): Collection<T> {
        val typedComponents = components[componentType]
        return typedComponents as Collection<T>
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : SingletonEcsComponent> getSingletonComponentByType(componentType: Class<T>): T? {
        return components
            .get(componentType)
            .firstOrNull() as T?
    }

    override fun getComponentsCount(): Int {
        return components.size()
    }

    override fun clear() {
        components.clear()
    }
}