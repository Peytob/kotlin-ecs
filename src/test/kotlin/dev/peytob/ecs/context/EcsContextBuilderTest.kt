package dev.peytob.ecs.context

import dev.peytob.ecs.EcsTests
import dev.peytob.ecs.system.EcsSystem
import dev.peytob.ecs.system.instance.TestSystem1
import dev.peytob.ecs.system.instance.TestSystem2
import dev.peytob.ecs.system.instance.TestSystem3
import org.junit.jupiter.api.Test

class EcsContextBuilderTest : EcsTests() {

    @Test
    fun justSystemAppendingMakesRightOrder() {
        val context = EcsContextBuilder()
            .appendSystem(TestSystem1())
            .appendSystem(TestSystem2())
            .appendSystem(TestSystem3())
            .build()

        assertElementsEquals(
            extractTypes(context.getSystems()),
            listOf(TestSystem1::class.java, TestSystem2::class.java, TestSystem3::class.java))
    }

    @Test
    fun systemAppendsAfterCorrectly() {
        val context = EcsContextBuilder()
            .appendSystem(TestSystem1())
            .appendSystem(TestSystem3())
            .appendSystemAfter(TestSystem2(), TestSystem1::class.java)
            .build()

        assertElementsEquals(
            extractTypes(context.getSystems()),
            listOf(TestSystem1::class.java, TestSystem2::class.java, TestSystem3::class.java))
    }

    @Test
    fun systemAppendsBeforeFirstElementCorrectly() {
        val context = EcsContextBuilder()
            .appendSystem(TestSystem1())
            .appendSystem(TestSystem3())
            .appendSystemBefore(TestSystem2(), TestSystem1::class.java)
            .build()

        assertElementsEquals(
            extractTypes(context.getSystems()),
            listOf(TestSystem2::class.java, TestSystem1::class.java, TestSystem3::class.java))
    }

    @Test
    fun systemAppendsAfterLastElementCorrectly() {
        val context = EcsContextBuilder()
            .appendSystem(TestSystem1())
            .appendSystem(TestSystem3())
            .appendSystemAfter(TestSystem2(), TestSystem3::class.java)
            .build()

        assertElementsEquals(
            extractTypes(context.getSystems()),
            listOf(TestSystem1::class.java, TestSystem3::class.java, TestSystem2::class.java))
    }

    @Test
    fun systemAppendsWithIndexesCorrectly() {
        val context = EcsContextBuilder()
            .appendSystem(TestSystem1(), 10)
            .appendSystem(TestSystem3(), 12)
            .appendSystem(TestSystem2(), 11)
            .build()

        assertElementsEquals(
            extractTypes(context.getSystems()),
            listOf(TestSystem1::class.java, TestSystem2::class.java, TestSystem3::class.java))
    }

    private fun extractTypes(systems: Collection<EcsSystem>): Collection<Class<out EcsSystem>> {
        return systems.map { it.javaClass }
    }
}
