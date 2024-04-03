package dev.peytob.ecs.event

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap

class SimpleEventManager : EventManager {

    private val events: Multimap<Class<out EcsEvent>, EcsEvent> = HashMultimap.create()

    override fun appendEvent(event: EcsEvent) {
        events.put(event.javaClass, event)
    }

    override fun removeEvent(event: EcsEvent) {
        events.remove(event.javaClass, event)
    }

    override fun removeAllEventsByType(eventType: Class<out EcsEvent>) {
        events.removeAll(eventType)
    }

    override fun getEventTypes(): Collection<Class<out EcsEvent>> {
        return events.keySet()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : EcsEvent> getEventsByType(eventType: Class<T>): Collection<T> {
        return events[eventType] as Collection<T>
    }

    override fun getEventsCount(): Int {
        return events.size()
    }

    override fun clear() {
        events.clear()
    }
}