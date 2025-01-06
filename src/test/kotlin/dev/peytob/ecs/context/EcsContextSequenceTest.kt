package dev.peytob.ecs.context

import dev.peytob.ecs.EcsTests
import dev.peytob.ecs.system.instance.TestSystem1
import dev.peytob.ecs.system.instance.TestSystem2
import dev.peytob.ecs.system.instance.TestSystem3
import dev.peytob.ecs.system.instance.TestSystem4
import org.junit.jupiter.api.Test

class EcsContextSequenceTest : EcsTests() {

    @Test
    fun sequenceAppendBeforeInsertsInRightOrder() {
        val context = EcsContextBuilder()
            .appendSystem(TestSystem1())
            .appendSystem(TestSystem2())
            .appendSystem(TestSystem3())

            .beginSequenceBefore(TestSystem2::class.java)
            .next(TestSystem4())
            .endSequence()

            .build()

        assertElementsEquals(
            extractTypes(context.getSystems()),
            listOf(TestSystem1::class.java, TestSystem4::class.java, TestSystem2::class.java, TestSystem3::class.java))
    }

    @Test
    fun sequenceAppendAfterInsertsInRightOrder() {
        val context = EcsContextBuilder()
            .appendSystem(TestSystem1())
            .appendSystem(TestSystem2())
            .appendSystem(TestSystem3())

            .beginSequenceAfter(TestSystem2::class.java)
            .next(TestSystem4())
            .endSequence()

            .build()

        assertElementsEquals(
            extractTypes(context.getSystems()),
            listOf(TestSystem1::class.java, TestSystem2::class.java, TestSystem4::class.java, TestSystem3::class.java))
    }

    @Test
    fun sequenceAppendIndexedInsertsInRightOrder() {
        val context = EcsContextBuilder()
            .appendSystem(TestSystem1())
            .appendSystem(TestSystem2())
            .appendSystem(TestSystem3())

            .beginSequence(1)
            .next(TestSystem4())
            .endSequence()

            .build()

        assertElementsEquals(
            extractTypes(context.getSystems()),
            listOf(TestSystem1::class.java, TestSystem2::class.java, TestSystem4::class.java, TestSystem3::class.java))
    }
}
