package dev.peytob.ecs.exception

import dev.peytob.ecs.entity.EcsEntity

class EcsEntityAlreadyExistsException(
    message: String,
    val entity: EcsEntity
) : RuntimeException()
