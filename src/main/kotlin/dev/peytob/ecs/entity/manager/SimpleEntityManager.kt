package dev.peytob.ecs.entity.manager

import dev.peytob.ecs.entity.EcsEntity
import dev.peytob.ecs.exception.EcsEntityAlreadyExistsException

class SimpleEntityManager : EntityManager {

    private var entities: MutableMap<String, EcsEntity> = mutableMapOf()

    override fun appendEntity(entity: EcsEntity) {
        if (entities.containsKey(entity.id)) {
            throw EcsEntityAlreadyExistsException("Entity with this id is already exists", entity)
        }

        entities.put(entity.id, entity)
    }

    override fun removeEntity(entity: EcsEntity) {
        entities.remove(entity.id)
    }

    override fun getEntityById(entityId: String): EcsEntity? {
        return entities[entityId]
    }

    override fun getAllEntities(): Collection<EcsEntity> {
        return entities.values
    }

    override fun getEntitiesCount(): Int {
        return entities.size
    }

    override fun clear() {
        entities.clear()
    }
}