package dev.peytob.ecs.entity

import java.awt.Component

interface EcsEntity {

    val id: String

    fun getComponents(): Component

    fun <C : Component> getComponentTypes(): Collection<Class<C>>

    fun <T : Component?> removeComponent(componentType: Class<T>): T

    fun bindComponent(component: Component)

    fun isEmpty(): Boolean

    fun isAlive(): Boolean
}
