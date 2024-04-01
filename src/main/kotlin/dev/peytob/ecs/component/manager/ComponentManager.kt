package dev.peytob.ecs.component.manager

import dev.peytob.ecs.component.EcsComponent
import dev.peytob.ecs.component.SingletonEcsComponent

interface ComponentManager {

    fun appendComponent(ecsComponent: EcsComponent)

    fun removeComponent(ecsComponent: EcsComponent)

    fun getComponentTypes(): Collection<Class<out EcsComponent>>

    fun <T : EcsComponent> getComponentsByType(componentType: Class<T>): Collection<T>

    fun <T : SingletonEcsComponent> getSingletonComponentByType(componentType: Class<T>): T?

    fun getComponentsCount(): Int

    fun clear()
}