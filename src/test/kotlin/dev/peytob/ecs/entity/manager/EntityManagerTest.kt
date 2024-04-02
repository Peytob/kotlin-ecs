package dev.peytob.ecs.entity.manager

import dev.peytob.ecs.EcsTests
import dev.peytob.ecs.entity.EcsEntity
import dev.peytob.ecs.exception.EcsEntityAlreadyExistsException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

abstract class EntityManagerTest : EcsTests() {

    lateinit var entityManager: EntityManager

    abstract fun createInstance(): EntityManager

    @BeforeEach
    fun setUpComponentManagerInstance() {
        entityManager = createInstance()
    }

    @Test
    fun newEntityManagerIsEmpty() {
        assertTrue(entityManager.getAllEntities().isEmpty())
    }

    @Test
    fun entitySuccessfullyRegistered() {
        val entity = createEntity("someEntity")

        assertDoesNotThrow {
            entityManager.appendEntity(entity)
        }

        assertEquals(1, entityManager.getEntitiesCount())
    }

    @Test
    fun entitySuccessfullyRemoved() {
        val first = createEntity("a")
        val second = createEntity("b")
        val third = createEntity("c")

        entityManager.appendEntity(first)
        entityManager.appendEntity(second)
        entityManager.appendEntity(third)
        entityManager.removeEntity(second)

        assertElementsEquals(listOf(first, third), entityManager.getAllEntities())
    }

    @Test
    fun entityManagerIsEmptyAfterClear() {
        entityManager.appendEntity(createEntity("a"))
        entityManager.appendEntity(createEntity("b"))
        entityManager.appendEntity(createEntity("c"))

        assertEquals(3, entityManager.getEntitiesCount())

        entityManager.clear()

        assertTrue(entityManager.getAllEntities().isEmpty())
        assertEquals(0, entityManager.getEntitiesCount())
    }

    @Test
    fun throwsExceptionIfEntityWithSomeIdExists() {
        val id = "someid"
        val entity = createEntity(id)
        val otherEntityWithSomeId = createEntity(id)

        entityManager.appendEntity(entity)

        assertThrows(EcsEntityAlreadyExistsException::class.java) {
            entityManager.appendEntity(otherEntityWithSomeId)
        }
    }

    @Test
    fun entityCantBeRegisteredTwoTimes() {
        val entity = createEntity("sdfa")

        entityManager.appendEntity(entity)

        assertThrows(EcsEntityAlreadyExistsException::class.java) {
            entityManager.appendEntity(entity)
        }
    }

    private fun createEntity(id: String): EcsEntity {
        val mock = mock(EcsEntity::class.java, "Entity-$id")
        `when`(mock.id).thenReturn(id)
        return mock
    }

}