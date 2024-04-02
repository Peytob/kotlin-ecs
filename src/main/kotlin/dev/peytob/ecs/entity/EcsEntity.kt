package dev.peytob.ecs.entity

import dev.peytob.ecs.component.EcsComponent

interface EcsEntity {

    val id: String

    fun getComponents(): Collection<EcsComponent>

    fun getComponentTypes(): Collection<Class<out EcsComponent>>

    fun <T : EcsComponent> getComponent(componentType: Class<T>): T?

    fun <T : EcsComponent> removeComponent(componentType: Class<T>): T?

    fun <T : EcsComponent> containsComponent(componentType: Class<T>): Boolean

    fun appendComponent(component: EcsComponent)

    fun isEmpty(): Boolean

    fun isAlive(): Boolean
}
