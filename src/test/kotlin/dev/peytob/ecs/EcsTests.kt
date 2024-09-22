package dev.peytob.ecs

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

open class EcsTests {

    protected open fun <E> assertElementsEquals(expected: Collection<E>, actual: Collection<E>) {
        assertEquals(expected.size, actual.size)
        assertTrue(expected.containsAll(actual))
    }
}