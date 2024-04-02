package dev.peytob.ecs.system.manager

import dev.peytob.ecs.EcsTests
import dev.peytob.ecs.exception.EcsSystemAlreadyExistsException
import dev.peytob.ecs.system.EcsSystem
import dev.peytob.ecs.system.instance.TestSystem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class SystemManagerTest : EcsTests() {

    private lateinit var systemManager: SystemManager

    abstract fun createInstance(): SystemManager

    @BeforeEach
    fun setUpSystemManagerInstance() {
        systemManager = createInstance()
    }

    @Test
    fun newSystemManagerIsEmpty() {
        assertTrue(systemManager.getAllSystems().isEmpty())
    }

    @Test
    fun systemSuccessfullyRegistered() {
        val system = EcsSystem {}

        systemManager.appendSystem(system)

        assertElementsEquals(setOf(system), systemManager.getAllSystems())
        assertTrue(systemManager.containsSystem(system))
    }

    @Test
    fun systemManagerCanFindSystemByClass() {
        val firstSystem = EcsSystem {}
        val secondSystem = EcsSystem {}

        systemManager.appendSystem(firstSystem)
        systemManager.appendSystem(secondSystem)

        systemManager.containsSystem(secondSystem.javaClass)
    }

    @Test
    fun systemManagerCanFindSystemByObject() {
        val firstSystem = EcsSystem {}
        val secondSystem = EcsSystem {}

        systemManager.appendSystem(firstSystem)
        systemManager.appendSystem(secondSystem)
        systemManager.containsSystem(firstSystem)
    }

    @Test
    fun systemSuccessfullyRemoved() {
        val firstSystem = EcsSystem {}
        val secondSystem = EcsSystem {}
        val thirdSystem = EcsSystem {}

        systemManager.appendSystem(firstSystem)
        systemManager.appendSystem(secondSystem)
        systemManager.appendSystem(thirdSystem)

        Assertions.assertDoesNotThrow { systemManager.removeSystem(secondSystem) }
        assertElementsEquals(listOf(firstSystem, thirdSystem), systemManager.getAllSystems())
    }

    @Test
    fun systemManagerIsEmptyAfterClear() {
        systemManager.appendSystem {}
        systemManager.appendSystem {}
        systemManager.appendSystem {}

        systemManager.clear()

        assertTrue(systemManager.getAllSystems().isEmpty())
    }

    @Test
    fun throwsExceptionIfSystemWithSomeClassAlreadyRegistered() {
        val firstSystem = TestSystem()
        val secondSystem = TestSystem()
        val thirdSystem = TestSystem()

        systemManager.appendSystem(firstSystem)

        assertThrows(EcsSystemAlreadyExistsException::class.java) {
            systemManager.appendSystem(secondSystem)
        }

        assertThrows(EcsSystemAlreadyExistsException::class.java) {
            systemManager.appendSystem(thirdSystem)
        }
    }

    @Test
    fun systemStoredInRightOrder() {
        val firstSystem = EcsSystem {}
        val secondSystem = EcsSystem {}
        val thirdSystem = EcsSystem {}

        systemManager.appendSystem(secondSystem)
        systemManager.appendSystem(firstSystem)
        systemManager.appendSystem(thirdSystem)

        assertElementsEquals(listOf(firstSystem, secondSystem, thirdSystem), systemManager.getAllSystems())
    }

    @Test
    fun throwsExceptionIfSystemObjectAlreadyRegistered() {
        val system = EcsSystem {}
        systemManager.appendSystem(system)

        assertThrows(EcsSystemAlreadyExistsException::class.java) {
            systemManager.appendSystem(system)
        }
    }
}