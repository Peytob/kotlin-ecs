package dev.peytob.ecs.entity.manager

import dev.peytob.ecs.EcsTests
import dev.peytob.ecs.component.instance.FirstTestComponent
import dev.peytob.ecs.component.instance.SecondTestComponent
import dev.peytob.ecs.entity.EcsEntity
import dev.peytob.ecs.exception.EcsComponentAlreadyExistsException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class EcsEntityTest : EcsTests() {

    private lateinit var entity: EcsEntity

    abstract fun createTestingImplementationEntity(): EcsEntity

    @BeforeEach
    open fun setUp() {
        entity = createTestingImplementationEntity()
    }

    @Test
    open fun isNewEntityIsEmpty() {
        assertTrue(entity.getComponents().isEmpty())
    }

    @Test
    open fun oneComponentSuccessfullyRegistered() {
        val testComponent1 = FirstTestComponent()
        val componentType = testComponent1.javaClass

        entity.appendComponent(testComponent1)

        assertEquals(1, entity.getComponents().size)
        assertNotNull(entity.getComponent(componentType))
    }

    @Test
    open fun multipleComponentsSuccessfullyRegistered() {
        val testComponent1 = FirstTestComponent()
        val testComponent2 = SecondTestComponent()

        entity.appendComponent(testComponent1)
        entity.appendComponent(testComponent2)

        assertEquals(2, entity.getComponents().size)
        assertNotNull(entity.getComponent(testComponent1.javaClass))
        assertNotNull(entity.getComponent(testComponent2.javaClass))
    }

    @Test
    open fun entityExceptionThrowsIfDuplicateComponentRegistered() {
        val testComponent11 = FirstTestComponent()
        val testComponent12 = FirstTestComponent()

        assertDoesNotThrow {
            entity.appendComponent(testComponent11)
        }

        assertThrows(EcsComponentAlreadyExistsException::class.java) {
            entity.appendComponent(testComponent12)
        }
    }

    @Test
    open fun componentSuccessfullyRemoved() {
        val testComponent1 = FirstTestComponent()
        val testComponent2 = SecondTestComponent()

        entity.appendComponent(testComponent1)
        entity.appendComponent(testComponent2)

        assertEquals(2, entity.getComponents().size)
        assertNotNull(entity.getComponent(testComponent1.javaClass))
        assertNotNull(entity.getComponent(testComponent2.javaClass))

        entity.removeComponent(testComponent1.javaClass)

        assertEquals(1, entity.getComponents().size)
        assertNull(entity.getComponent(testComponent1.javaClass))
        assertNotNull(entity.getComponent(testComponent2.javaClass))
    }

}