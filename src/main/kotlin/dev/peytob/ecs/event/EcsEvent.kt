package dev.peytob.ecs.event

interface EcsEvent {

    /**
     * Indicates that event should not be removed after processing / frame / etc. Useful for
     * your game logic
     */
    val keep: Boolean
        get() = false
}
