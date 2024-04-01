package dev.peytob.ecs.component.manager

class SimpleComponentManagerTest : ComponentManagerTest() {

    override fun createInstance(): ComponentManager = SimpleComponentManager()
}