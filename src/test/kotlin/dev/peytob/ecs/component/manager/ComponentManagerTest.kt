package dev.peytob.ecs.component.manager

import dev.peytob.ecs.EcsTests
import dev.peytob.ecs.component.EcsComponent
import dev.peytob.ecs.component.instance.FirstSingletonComponent
import dev.peytob.ecs.component.instance.FirstTestComponent
import dev.peytob.ecs.component.instance.SecondSingletonComponent
import dev.peytob.ecs.component.instance.SecondTestComponent
import dev.peytob.ecs.exception.EcsComponentAlreadyExistsException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class ComponentManagerTest : EcsTests() {

    private lateinit var componentManager: ComponentManager

    abstract fun createInstance(): ComponentManager

    @BeforeEach
    fun setUpComponentManagerInstance() {
        componentManager = createInstance()
    }

    @Test
    fun componentsManagerIsClearAfterCreating() {
        assertElementsEquals(emptyList(), componentManager.getComponentTypes())
        assertEquals(0, componentManager.getComponentsCount())
    }

    @Test
    fun returnsEmptyCollectionAtNotExistsType() {
        assertIterableEquals(
            emptyList<Any>(),
            componentManager.getComponentsByType(FirstTestComponent::class.java)
        )
    }

    @Test
    fun oneComponentSuccessfullyRegistered() {
        val component = FirstTestComponent()
        val componentClass = FirstTestComponent::class.java

        componentManager.appendComponent(component)

        assertElementsEquals(componentManager.getComponentTypes(), setOf(componentClass))
        assertElementsEquals(componentManager.getComponentsByType(componentClass), setOf(component))
    }

    @Test
    fun manyComponentsWithSingleTypeSuccessfullyRegistered() {
        val component1 = FirstTestComponent()
        val component2 = FirstTestComponent()
        val component3 = FirstTestComponent()
        val componentClass = FirstTestComponent::class.java

        componentManager.appendComponent(component1)
        componentManager.appendComponent(component2)
        componentManager.appendComponent(component3)

        assertEquals(3, componentManager.getComponentsCount())
        assertElementsEquals(setOf(componentClass), componentManager.getComponentTypes())
        assertElementsEquals(listOf(component1, component2, component3), componentManager.getComponentsByType(componentClass))
    }

    @Test
    fun manyComponentsWithDifferentTypesSuccessfullyRegistered() {
        val firstComponent = FirstTestComponent()
        val secondComponent = SecondTestComponent()

        componentManager.appendComponent(firstComponent)
        componentManager.appendComponent(secondComponent)

        assertEquals(2, componentManager.getComponentsCount())
        assertElementsEquals(listOf(firstComponent.javaClass, secondComponent.javaClass), componentManager.getComponentTypes())
        assertElementsEquals(setOf(firstComponent), componentManager.getComponentsByType(FirstTestComponent::class.java))
        assertElementsEquals(setOf(secondComponent), componentManager.getComponentsByType(SecondTestComponent::class.java))
    }

    @Test
    fun typeDisappearsAfterRemovingLastTypeComponent() {
        val firstComponent: EcsComponent = FirstTestComponent()
        val secondComponent: EcsComponent = SecondTestComponent()

        componentManager.appendComponent(firstComponent)
        componentManager.appendComponent(secondComponent)
        componentManager.removeComponent(firstComponent)

        assertElementsEquals(setOf(secondComponent.javaClass), componentManager.getComponentTypes())
        assertIterableEquals(emptyList<Any>(), componentManager.getComponentsByType(firstComponent.javaClass))
        assertEquals(1, componentManager.getComponentsCount())
    }

    @Test
    fun componentManagerDeletesAllElementsAfterClearing() {
        val firstComponent: EcsComponent = FirstTestComponent()
        val secondComponent: EcsComponent = SecondTestComponent()

        componentManager.appendComponent(firstComponent)
        componentManager.appendComponent(secondComponent)
        componentManager.clear()

        assertElementsEquals(emptyList(), componentManager.getComponentTypes())
        assertEquals(0, componentManager.getComponentsCount())
    }

    @Test
    fun componentManagerThrowsExceptionIfComponentAlreadyExists() {
        val component: EcsComponent = FirstTestComponent()

        componentManager.appendComponent(component)

        assertThrows(EcsComponentAlreadyExistsException::class.java) {
            componentManager.appendComponent(component)
        }
    }

    @Test
    fun singletonComponentCanBeRegistered() {
        val singletonComponent = FirstSingletonComponent()

        assertDoesNotThrow {
            componentManager.appendComponent(singletonComponent)
        }
    }

    @Test
    fun otherSingletonComponentCanBeRegisteredAfterRemovingFirst() {
        val singletonComponent1 = FirstSingletonComponent()
        val singletonComponent2 = FirstSingletonComponent()

        assertDoesNotThrow {
            componentManager.appendComponent(singletonComponent1)
        }

        assertThrows(EcsComponentAlreadyExistsException::class.java) {
            componentManager.appendComponent(singletonComponent2)
        }
    }

    @Test
    fun twoSameSingletonComponentCantBeRegistered() {
        val singletonComponent1 = FirstSingletonComponent()
        val singletonComponent2 = FirstSingletonComponent()

        componentManager.appendComponent(singletonComponent1)
        componentManager.removeComponent(singletonComponent1)

        assertDoesNotThrow {
            componentManager.appendComponent(singletonComponent2)
        }
    }

    @Test
    fun twoDifferentSingletonCanBeRegistered() {
        val singletonComponent1 = FirstSingletonComponent()
        val singletonComponent2 = SecondSingletonComponent()

        assertDoesNotThrow {
            componentManager.appendComponent(singletonComponent1)
        }

        assertDoesNotThrow {
            componentManager.appendComponent(singletonComponent2)
        }
    }
}