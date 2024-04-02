package dev.peytob.ecs.entity.manager

import dev.peytob.ecs.entity.EcsEntity
import dev.peytob.ecs.entity.GenericEcsEntity

class GenericEcsEntityTest : EcsEntityTest() {

    override fun createTestingImplementationEntity(): EcsEntity {
        return GenericEcsEntity("generic")
    }
}