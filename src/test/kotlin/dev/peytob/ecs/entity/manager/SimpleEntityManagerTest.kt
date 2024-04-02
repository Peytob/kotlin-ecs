package dev.peytob.ecs.entity.manager

class SimpleEntityManagerTest : EntityManagerTest() {
    override fun createInstance(): EntityManager {
        return SimpleEntityManager()
    }
}