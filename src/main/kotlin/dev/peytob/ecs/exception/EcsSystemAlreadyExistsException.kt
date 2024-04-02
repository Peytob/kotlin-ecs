package dev.peytob.ecs.exception

import dev.peytob.ecs.system.EcsSystem

class EcsSystemAlreadyExistsException(
    message: String,
    val ecsSystem: EcsSystem
) : RuntimeException(message)
