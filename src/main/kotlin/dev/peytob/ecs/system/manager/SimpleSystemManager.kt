package dev.peytob.ecs.system.manager

import dev.peytob.ecs.exception.EcsSystemAlreadyExistsException
import dev.peytob.ecs.system.EcsSystem

class SimpleSystemManager : SystemManager {

    private val systems: MutableCollection<EcsSystem> = mutableListOf()

    private val systemTypes: MutableCollection<Class<out EcsSystem>> = mutableListOf()

    override fun getAllSystems(): Collection<EcsSystem> {
        return systems
    }

    override fun appendSystem(ecsSystem: EcsSystem) {
        if (containsSystem(ecsSystem)) {
            throw EcsSystemAlreadyExistsException("System already exists", ecsSystem)
        }

        systems.add(ecsSystem)
        systemTypes.add(ecsSystem.javaClass)
    }

    override fun removeSystem(ecsSystem: EcsSystem) {
        systems.remove(ecsSystem)
        systemTypes.remove(ecsSystem.javaClass)
    }

    override fun <T : EcsSystem> containsSystem(ecsSystemType: Class<T>): Boolean {
        return systemTypes.contains(ecsSystemType)
    }

    override fun containsSystem(ecsSystem: EcsSystem): Boolean {
        return containsSystem(ecsSystem.javaClass)
    }

    override fun getSystemsCount(): Int {
        return systems.size
    }

    override fun clear() {
        systems.clear()
        systemTypes.clear()
    }
}