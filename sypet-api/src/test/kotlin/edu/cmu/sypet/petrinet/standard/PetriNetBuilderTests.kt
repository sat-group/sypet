package edu.cmu.sypet.petrinet.standard

import edu.cmu.sypet.java.MethodSignature
import edu.cmu.sypet.java.Type
import edu.cmu.sypet.petrinet.BadCastException
import edu.cmu.sypet.petrinet.NoSuchPlaceException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PetriNetBuilderTests {

    private fun createBuilder(): PetriNetBuilder {
        return PetriNetBuilder(mockk())
    }

    //region Tests
    @Nested
    inner class AddPlace {

        @Test
        fun `is idempotent`() {
            `is idempotent`(mockk<Type>(), PetriNetBuilder::addPlace)
        }

        @Test
        fun `the resulting Petri net contains the place`() {
            `Petri net contains`(mockk<Type>(), PetriNetBuilder::addPlace, PetriNetRead::contains)
        }
    }

    @Nested
    inner class AddTransition {

        @Test
        fun `is idempotent`() {
            `is idempotent`(node = mockk<MethodSignature>()) { addTransition(it) }
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            `Petri net contains`(mockk(), PetriNetBuilder::addTransition, PetriNetRead::contains)
        }

        @Test
        fun `throws if a type is missing`() {
            `throws if a type is missing`(mockk(), PetriNetBuilder::addTransition)
        }
    }

    @Nested
    inner class AddVoidTransition {

        @Test
        fun `is idempotent`() {
            `is idempotent`(mockk<MethodSignature>()) { addVoidTransition(it) }
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            `Petri net contains`(mockk(), PetriNetBuilder::addTransition, PetriNetRead::contains)
        }

        @Test
        fun `throws if types are missing`() {
            `throws if a type is missing`(mockk(), PetriNetBuilder::addVoidTransition)
        }
    }

    @Nested
    inner class AddCloneTransition {

        @Test
        fun `is idempotent`() {
            `is idempotent`(mockk<Type>()) { addCloneTransition(it) }
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            `Petri net contains`(mockk(), PetriNetBuilder::addTransition, PetriNetRead::contains)
        }

        @Test
        fun `throws if a type is missing`() {
            `throws if a type is missing`(mockk<Type>(), PetriNetBuilder::addCloneTransition)
        }

    }

    @Nested
    inner class AddCastTransition {

        @Test
        fun `is idempotent`() {
            `is idempotent`(mockk<Type>(), mockk(), PetriNetBuilder::addCastTransition)
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            `Petri net contains`(mockk(), PetriNetBuilder::addTransition, PetriNetRead::contains)
        }

        @Test
        fun `throws if a type is missing`() {
            val type1 = mockk<Type>()
            val type2 = mockk<Type>()

            every { type1 != type2 } returns false

            `throws if a type is missing`(type1, type2, PetriNetBuilder::addCastTransition)
        }

        @Test
        fun `throws if the types are not castable`() {
            val builder = createBuilder()

            val type1 = mockk<Type>()
            val type2 = mockk<Type>()

            every { type1.isCastableTo(type2) } returns false

            assertThrows<BadCastException> { builder.addCastTransition(type1, type2) }
        }
    }
    //endregion

    //region Helpers
    private fun <T> `is idempotent`(
        node: T,
        add: PetriNetBuilder.(T) -> PetriNetBuilder
    ) {
        val builder = createBuilder()

        val net1 = builder.add(node).build()
        val net2 = builder.add(node).build()

        assertEquals(expected = net1, actual = net2)
    }

    private fun <T, U> `is idempotent`(
        node1: T,
        node2: U,
        add: PetriNetBuilder.(T, U) -> PetriNetBuilder
    ) {
        val builder = createBuilder()

        val net1 = builder.add(node1, node2).build()
        val net2 = builder.add(node1, node2).build()

        assertEquals(expected = net1, actual = net2)
    }

    private fun <T> `Petri net contains`(
        node: T,
        add: PetriNetBuilder.(T) -> PetriNetBuilder,
        contains: PetriNetRead.(T) -> Boolean
    ) {
        val builder = createBuilder()

        val net = builder.add(node).build()

        assertTrue(net.contains(node))
    }

    private fun <T> `throws if a type is missing`(
        node: T,
        add: PetriNetBuilder.(T) -> PetriNetBuilder
    ) {
        val builder = createBuilder()

        val action = { builder.add(node) }

        assertThrows<NoSuchPlaceException> { action }
    }

    private fun <T, U> `throws if a type is missing`(
        node1: T,
        node2: U,
        add: PetriNetBuilder.(T, U) -> PetriNetBuilder
    ) {
        val builder = createBuilder()

        val action = { builder.add(node1, node2) }

        assertThrows<NoSuchPlaceException> { action }
    }
    //endregion
}
