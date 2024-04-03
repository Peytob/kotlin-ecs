package dev.peytob.ecs.event

interface EventManager {

    fun appendEvent(event: EcsEvent)

    fun removeEvent(event: EcsEvent)

    fun removeAllEventsByType(eventType: Class<out EcsEvent>)

    fun getEventTypes(): Collection<Class<out EcsEvent>>

    fun <T : EcsEvent> getEventsByType(eventType: Class<T>): Collection<T>

    fun getEventsCount(): Int

    fun clear()
}
