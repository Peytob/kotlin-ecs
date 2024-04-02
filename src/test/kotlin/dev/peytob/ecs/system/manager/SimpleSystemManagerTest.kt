package dev.peytob.ecs.system.manager

class SimpleSystemManagerTest : SystemManagerTest() {

    override fun createInstance(): SystemManager {
        return SimpleSystemManager()
    }
}