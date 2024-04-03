package dev.peytob.ecs.context.generator

import org.apache.commons.lang3.RandomStringUtils

class RandomStringEcsEntityIdGenerator : EcsEntityIdGenerator {

    override fun generateNextId(): String = RandomStringUtils.random(12)
}