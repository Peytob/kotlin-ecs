package dev.peytob.ecs.event

import com.google.common.collect.Multimap
import com.google.common.collect.Multimaps
import java.util.concurrent.ConcurrentHashMap

class SimpleEventManager : EventManager {

    private val events: Multimap<Class<out EcsEvent>, EcsEvent> = Multimaps.newListMultimap(ConcurrentHashMap(), ::mutableListOf)

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