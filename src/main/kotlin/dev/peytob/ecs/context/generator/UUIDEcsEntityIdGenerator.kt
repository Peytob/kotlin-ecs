package dev.peytob.ecs.context.generator

import java.util.*

class UUIDEcsEntityIdGenerator : EcsEntityIdGenerator {

    override fun generateNextId(): String = UUID.randomUUID().toString()
}