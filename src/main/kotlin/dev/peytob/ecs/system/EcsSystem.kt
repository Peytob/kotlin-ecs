package dev.peytob.ecs.system

import dev.peytob.ecs.context.EcsContext

interface EcsSystem {

    fun execute(ecsContext: EcsContext)
}
