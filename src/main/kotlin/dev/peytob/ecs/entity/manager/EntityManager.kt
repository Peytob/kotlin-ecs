package dev.peytob.ecs.entity.manager

import dev.peytob.ecs.entity.EcsEntity

interface EntityManager {

    fun appendEntity(entity: EcsEntity)

    fun removeEntity(entity: EcsEntity)

    fun getEntityById(entityId: String): EcsEntity?

    fun getAllEntities(): Collection<EcsEntity>

    fun getEntitiesCount(): Int

    fun clear()
}