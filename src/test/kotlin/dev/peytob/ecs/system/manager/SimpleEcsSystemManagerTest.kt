package dev.peytob.ecs.system.manager

class SimpleEcsSystemManagerTest : EcsSystemManagerTest() {

    override fun createInstance(): EcsSystemManager {
        return SimpleEcsSystemManager()
    }
}