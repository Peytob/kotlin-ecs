package dev.peytob.ecs.system

import dev.peytob.ecs.context.EcsContext

fun interface EcsSystem {

    fun execute(ecsContext: EcsContext)
}
