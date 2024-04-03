package dev.peytob.ecs.context

import dev.peytob.ecs.EcsTests
import dev.peytob.ecs.component.instance.FirstTestComponent
import dev.peytob.ecs.component.instance.SecondTestComponent
import dev.peytob.ecs.event.instance.FirstEvent
import dev.peytob.ecs.event.instance.SecondEvent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class EcsContextTest : EcsTests() {

    private lateinit var ecsContext: EcsContext

    abstract fun createInstance(): EcsContext

    @BeforeEach
    open fun setUpEcsContextMethodInstance() {
        ecsContext = createInstance()
    }

    @Test
    open fun newContextIsEmpty() {
        assertTrue(ecsContext.getAllEntities().isEmpty())
        assertTrue(ecsContext.getComponentTypes().isEmpty())
    }

    @Test
    open fun newEntitySuccessfullyCreated() {
        val entity = ecsContext.createEntity("newEntitySuccessfullyCreated")

        assertIterableEquals(setOf<Any>(entity), ecsContext.getAllEntities())
    }

    @Test
    open fun contextEntitiesNotifiesContextAboutNewComponents() {
        val entity = ecsContext.createEntity("contextEntitiesNotifiesContextAboutNewComponents")
        val component = SecondTestComponent()

        entity.appendComponent(component)

        assertIterableEquals(setOf<Any>(component), ecsContext.getComponentsByType(component.javaClass))
    }

    @Test
    open fun contextEntitiesNotifiesContextAboutNewComponentRemoving() {
        val entity = ecsContext.createEntity("contextEntitiesNotifiesContextAboutNewComponentRemoving")
        val first = FirstTestComponent()
        val second = SecondTestComponent()

        entity.appendComponent(first)
        entity.appendComponent(second)

        assertElementsEquals(setOf(first), ecsContext.getComponentsByType(FirstTestComponent::class.java))
        assertElementsEquals(setOf(second), ecsContext.getComponentsByType(SecondTestComponent::class.java))

        entity.removeComponent(first.javaClass)

        assertTrue(ecsContext.getComponentsByType(first.javaClass).isEmpty())
        assertElementsEquals(setOf(second), ecsContext.getComponentsByType(SecondTestComponent::class.java))
    }

    @Test
    open fun returnsEmptyOptionalIfEntityIdNotExists() {
        assertNull(ecsContext.getEntityById("notExistsIds"))
    }

    @Test
    open fun returnsCorrectEntityById() {
        ecsContext.createEntity("wrongEntityId")
        val expectedEntity = ecsContext.createEntity("expectedEntity")

        val foundEntity = ecsContext.getEntityById(expectedEntity.id)

        assertNotNull(foundEntity)
        assertSame(expectedEntity, foundEntity)
    }

    @Test
    open fun entityCanBeRemovedSuccessfully() {
        val entity = ecsContext.createEntity("entityCanBeRemovedSuccessfully")

        entity.appendComponent(FirstTestComponent())
        entity.appendComponent(SecondTestComponent())
        ecsContext.removeEntity(entity)

        assertNotNull(ecsContext.getEntityById(entity.id))
        assertTrue(ecsContext.getComponentTypes().isEmpty())
    }

    @Test
    open fun eventCanBeSuccessfullyAdded() {
        val event = FirstEvent()

        ecsContext.appendEvent(event)

        assertElementsEquals(setOf(FirstEvent::class.java), ecsContext.getEventTypes())
        assertElementsEquals(setOf(event), ecsContext.getEventsByType(FirstEvent::class.java))
    }

    @Test
    open fun eventCanBeSuccessfullyRemoved() {
        val event = FirstEvent()

        ecsContext.appendEvent(event)
        ecsContext.removeEvent(event)

        assertTrue(ecsContext.getEventsByType(FirstEvent::class.java).isEmpty())
    }

    @Test
    open fun clearEventsRemovesAllEvent() {
        val firstEvent = FirstEvent()
        val anotherFirstEvent = FirstEvent()
        val secondEvent = SecondEvent()

        ecsContext.appendEvent(firstEvent)
        ecsContext.appendEvent(anotherFirstEvent)
        ecsContext.appendEvent(secondEvent)
        ecsContext.clearEvents()

        assertTrue(ecsContext.getEventTypes().isEmpty())
        assertTrue(ecsContext.getEventsByType(FirstEvent::class.java).isEmpty())
        assertTrue(ecsContext.getEventsByType(SecondEvent::class.java).isEmpty())
    }

    @Test
    open fun contextCanReturnEntityBoundToComponent() {
        val firstEntity = ecsContext.createEntity("entityWithComponent")
        val secondEntity = ecsContext.createEntity("entityWithTwoComponents")
        ecsContext.createEntity("entityWithoutComponent")

        firstEntity.appendComponent(FirstTestComponent())
        secondEntity.appendComponent(FirstTestComponent())
        secondEntity.appendComponent(SecondTestComponent())

        firstEntity.getComponents().forEach { component ->
            val componentEntity = ecsContext.getComponentEntity(component)
            assertSame(firstEntity, componentEntity)
        }

        secondEntity.getComponents().forEach { component ->
            val componentEntity = ecsContext.getComponentEntity(component)
            assertSame(secondEntity, componentEntity)
        }
    }
}
