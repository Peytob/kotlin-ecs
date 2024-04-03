package dev.peytob.ecs.context.generator

fun interface EcsEntityIdGenerator {

    fun generateNextId(): String
}
