package dev.peytob.ecs

import org.junit.jupiter.api.Assertions

open class EcsTests {

    protected open fun <E> assertElementsEquals(expected: Collection<E>, actual: Collection<E>) {
        Assertions.assertEquals(expected.size, actual.size)
        Assertions.assertTrue(expected.containsAll(actual))
    }
}