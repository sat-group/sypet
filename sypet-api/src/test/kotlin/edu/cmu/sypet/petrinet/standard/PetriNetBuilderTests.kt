package edu.cmu.sypet.petrinet.standard

import edu.cmu.sypet.java.MethodSignature
import edu.cmu.sypet.java.Type
import edu.cmu.sypet.petrinet.BadCastException
import edu.cmu.sypet.petrinet.FrontendPetriNet
import edu.cmu.sypet.petrinet.NoSuchPlaceException
import edu.cmu.sypet.petrinet.backends.standard.BackendPetriNet
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private typealias PNBuilder = PetriNetBuilder<Type, MethodSignature<Type>>
private typealias MSignature = MethodSignature<Type>
private typealias FrontendPN = FrontendPetriNet<Type, MethodSignature<Type>>

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PetriNetBuilderTests {

    @Nested
    inner class AddPlace {

        @Test
        fun `is idempotent`() {
            `is idempotent`<Type>(mockk(), PNBuilder::addPlace)
        }

        @Test
        fun `the resulting Petri net contains the place`() {
            `Petri net contains`(mockk(), PNBuilder::addPlace, FrontendPN::containsPlace)
        }

    }

    @Nested
    inner class AddTransition {

        @Test
        fun `is idempotent`() {
            `is idempotent`(node = mockk<MSignature>()) { addTransition(it) }
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            `Petri net contains`(mockk(), PNBuilder::addTransition, FrontendPN::containsTransition)
        }

        @Test
        fun `throws if a type is missing`() {
            `throws if a type is missing`(mockk(), PNBuilder::addTransition)
        }

    }

    @Nested
    inner class AddVoidTransition {

        @Test
        fun `is idempotent`() {
            `is idempotent`(mockk<MSignature>()) { addVoidTransition(it) }
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            `Petri net contains`(mockk(), PNBuilder::addTransition, FrontendPN::containsTransition)
        }

        @Test
        fun `throws if types are missing`() {
            `throws if a type is missing`(mockk(), PNBuilder::addVoidTransition)
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
            `Petri net contains`(mockk(), PNBuilder::addTransition, FrontendPN::containsTransition)
        }

        @Test
        fun `throws if a type is missing`() {
            `throws if a type is missing`(mockk(), PNBuilder::addCloneTransition)
        }

    }

    @Nested
    inner class AddCastTransition {

        @Test
        fun `is idempotent`() {
            `is idempotent`(mockk<Type>(), mockk(), PNBuilder::addCastTransition)
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            `Petri net contains`(mockk(), PNBuilder::addTransition,  FrontendPN::containsTransition)
        }

        @Test
        fun `throws if a type is missing`() {
            val type1 = mockk<Type>()
            val type2 = mockk<Type>()

            every { type1 != type2 } returns false

            `throws if a type is missing`(type1, type2, PNBuilder::addCastTransition)
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

    private fun createBuilder(): PNBuilder {
        val petriNetWrite =
            BackendPetriNet(
                HashSet<Type>(),
                HashSet<MSignature>(),
                HashMap(),
                HashMap()
            )

        return PNBuilder(petriNetWrite)
    }

    private fun <T> `is idempotent`(
        node: T,
        add: PNBuilder.(T) -> PNBuilder
    ) {
        val builder = createBuilder()

        val net1 = builder.add(node).build()
        val net2 = builder.add(node).build()

        assertEquals(expected = net1, actual = net2)
    }

    private fun <T, U> `is idempotent`(
        node1: T,
        node2: U,
        add: PNBuilder.(T, U) -> PNBuilder
    ) {
        val builder = createBuilder()

        val net1 = builder.add(node1, node2).build()
        val net2 = builder.add(node1, node2).build()

        assertEquals(expected = net1, actual = net2)
    }

    private fun <T> `Petri net contains`(
        node: T,
        add: PNBuilder.(T) -> PNBuilder,
        contains: FrontendPN.(T) -> Boolean
    ) {
        val builder = createBuilder()

        val net = builder.add(node).build()

        assertTrue(net.contains(node))
    }

    private fun <T> `throws if a type is missing`(
        node: T,
        add: PNBuilder.(T) -> PNBuilder
    ) {
        val builder = createBuilder()

        val action = { builder.add(node) }

        assertThrows<NoSuchPlaceException> { action }
    }

    private fun <T, U> `throws if a type is missing`(
        node1: T,
        node2: U,
        add: PNBuilder.(T, U) -> PNBuilder
    ) {
        val builder = createBuilder()

        val action = { builder.add(node1, node2) }

        assertThrows<NoSuchPlaceException> { action }
    }
}
