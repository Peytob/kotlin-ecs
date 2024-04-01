package dev.peytob.ecs.exception

import dev.peytob.ecs.component.EcsComponent

class EcsComponentAlreadyExistsException(
    message: String,
    val ecsComponent: EcsComponent
) : RuntimeException(message)
