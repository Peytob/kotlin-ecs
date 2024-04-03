package dev.peytob.ecs.context

import dev.peytob.ecs.component.instance.FirstTestComponent
import dev.peytob.ecs.component.instance.SecondTestComponent
import dev.peytob.ecs.entity.EcsEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SimpleEcsContextTest : EcsContextTest() {

    override fun createInstance(): EcsContext {
        return EcsContextBuilder().build()
    }

    @Test
    fun contextEntityShouldSyncComponentsWithContextOnComponentBind() {
        val entity = ecsContext.createEntity("contextEntityShouldSyncComponentsWithContext")
        entity.appendComponent(FirstTestComponent())
        entity.appendComponent(SecondTestComponent())
        assertContextIsSyncedToSingletonEntity(ecsContext, entity)
    }

    @Test
    fun contextEntityShouldSyncComponentsWithContextOnComponentRemoved() {
        val entity = ecsContext.createEntity("contextEntityShouldSyncComponentsWithContext")
        entity.appendComponent(FirstTestComponent())
        entity.appendComponent(SecondTestComponent())
        entity.removeComponent(SecondTestComponent::class.java)
        assertContextIsSyncedToSingletonEntity(ecsContext, entity)
    }

    @Test
    fun contextUnbindsEntityComponentsAfterRemoving() {
        val component = FirstTestComponent()
        val entity = ecsContext.createEntity("contextUnbindsEntityComponentsAfterRemoving")
        entity.appendComponent(component)
        Assertions.assertSame(entity, ecsContext.getComponentEntity(component))
        entity.removeComponent(component.javaClass)
        Assertions.assertNull(ecsContext.getComponentEntity(component))
    }

    @Test
    fun contextEntityIsAliveWhileContextContainsIt() {
        val entity = ecsContext.createEntity("contextEntityShouldSyncComponentsWithContext")
        assertTrue(entity.isAlive())
        ecsContext.removeEntity(entity)
        assertFalse(entity.isAlive())
    }

    private fun assertContextIsSyncedToSingletonEntity(ecsContext: EcsContext, entity: EcsEntity) {
        assertElementsEquals(entity.getComponentTypes(), ecsContext.getComponentTypes())
        entity.getComponentTypes()
            .forEach { componentType ->
                val isContextContainsComponent = ecsContext
                    .getComponentsByType(componentType)
                    .contains(entity.getComponent(componentType))

                assertTrue(isContextContainsComponent)
            }
    }
}