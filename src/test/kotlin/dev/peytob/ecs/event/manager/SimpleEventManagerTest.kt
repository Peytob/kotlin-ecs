package dev.peytob.ecs.event.manager

import dev.peytob.ecs.event.EventManager
import dev.peytob.ecs.event.SimpleEventManager

class SimpleEventManagerTest : EventManagerTest() {

    override fun createInstance(): EventManager {
        return SimpleEventManager()
    }
}
