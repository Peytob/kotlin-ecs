package dev.peytob.ecs.system.manager

import dev.peytob.ecs.system.EcsSystem

interface EcsSystemManager {

    fun getAllSystems(): Collection<EcsSystem>

    fun appendSystem(ecsSystem: EcsSystem)

    fun removeSystem(ecsSystem: EcsSystem)

    fun <T : EcsSystem> containsSystem(ecsSystemType: Class<T>): Boolean

    fun containsSystem(ecsSystem: EcsSystem): Boolean

    fun getSystemsCount(): Int

    fun clear()
}