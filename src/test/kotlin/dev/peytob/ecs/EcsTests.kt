package dev.peytob.ecs

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.fail

open class EcsTests {

    protected open fun <E> assertElementsEquals(expected: Collection<E>, actual: Collection<E>) {
        Assertions.assertEquals(expected.size, actual.size)

        val zippedData = expected.zip(actual)

        val matches = zippedData.map { (e1, e2) ->
            e1 == e2
        }

        val allMatches = matches.all { it }

        // Additional debug output data
        if (!allMatches) {
            fail {
                val prefix = "Collection elements comparing result\n"
                matches.zip(zippedData).joinToString("\n", prefix) { (result, elements) ->
                    "${elements.first} == ${elements.second} -> $result"
                }
            }
        }
    }
}