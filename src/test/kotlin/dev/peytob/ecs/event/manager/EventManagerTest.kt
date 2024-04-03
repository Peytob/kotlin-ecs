package dev.peytob.ecs.event.manager

import dev.peytob.ecs.EcsTests
import dev.peytob.ecs.event.EventManager
import dev.peytob.ecs.event.instance.FirstEvent
import dev.peytob.ecs.event.instance.SecondEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

abstract class EventManagerTest : EcsTests() {

    private lateinit var eventManager: EventManager

    abstract fun createInstance(): EventManager

    @BeforeEach
    fun createEventManagerInstance() {
        eventManager = createInstance()
    }

    @Test
    fun eventManagerIsClearAfterCreating() {
        assertTrue(eventManager.getEventTypes().isEmpty())
        assertEquals(0, eventManager.getEventsCount())
    }

    @Test
    fun returnsEmptyCollectionAtNotExistsType() {
        assertTrue(eventManager.getEventsByType(FirstEvent::class.java).isEmpty())
    }

    @Test
    fun oneEventSuccessfullyRegistered() {
        val event = FirstEvent()
        val eventType = FirstEvent::class.java

        eventManager.appendEvent(event)

        assertElementsEquals(setOf(eventType), eventManager.getEventTypes())
        assertElementsEquals(setOf(event), eventManager.getEventsByType(eventType))
    }

    @Test
    fun manyEventsWithSingleTypeSuccessfullyRegistered() {
        val event1 = FirstEvent()
        val event2 = FirstEvent()
        val event3 = FirstEvent()
        val eventType = FirstEvent::class.java

        eventManager.appendEvent(event1)
        eventManager.appendEvent(event2)
        eventManager.appendEvent(event3)

        assertEquals(3, eventManager.getEventsCount())
        assertElementsEquals(setOf(eventType), eventManager.getEventTypes())
        assertElementsEquals(listOf(event1, event2, event3), eventManager.getEventsByType(eventType))
    }

    @Test
    fun manyEventsWithDifferentTypesSuccessfullyRegistered() {
        val firstEvent = FirstEvent()
        val secondEvent = SecondEvent()

        eventManager.appendEvent(firstEvent)
        eventManager.appendEvent(secondEvent)

        assertEquals(2, eventManager.getEventsCount())
        assertElementsEquals(listOf(firstEvent.javaClass, secondEvent.javaClass), eventManager.getEventTypes())
        assertElementsEquals(setOf(firstEvent), eventManager.getEventsByType(FirstEvent::class.java))
        assertElementsEquals(setOf(secondEvent), eventManager.getEventsByType(SecondEvent::class.java))
    }

    @Test
    fun typeDisappearsAfterRemovingLastTypeEvent() {
        val firstEvent = FirstEvent()
        val secondEvent = SecondEvent()

        eventManager.appendEvent(firstEvent)
        eventManager.appendEvent(secondEvent)
        eventManager.removeEvent(firstEvent)

        assertElementsEquals(setOf(secondEvent.javaClass), eventManager.getEventTypes())
        assertTrue(eventManager.getEventsByType(firstEvent.javaClass).isEmpty())
        assertEquals(1, eventManager.getEventsCount())
    }

    @Test
    fun eventManagerDeletesAllElementsAfterClearing() {
        val firstEvent = FirstEvent()
        val secondEvent = SecondEvent()

        eventManager.appendEvent(firstEvent)
        eventManager.appendEvent(secondEvent)
        eventManager.clear()

        assertTrue(eventManager.getEventTypes().isEmpty())
        assertEquals(0, eventManager.getEventsCount())
    }
}