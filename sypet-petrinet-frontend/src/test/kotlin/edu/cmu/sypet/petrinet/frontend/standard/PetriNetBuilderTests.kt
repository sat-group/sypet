package edu.cmu.sypet.petrinet.frontend.standard

import edu.cmu.sypet.java.MethodSignature
import edu.cmu.sypet.java.Type
import edu.cmu.sypet.petrinet.backend.standard.BackendPetriNet
import edu.cmu.sypet.petrinet.frontend.FrontendPetriNet
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
            `is idempotent`(type, PNBuilder::addPlace)
        }

        @Test
        fun `the resulting Petri net contains the place`() {
            val builder = createBuilder()

            val net = builder.addPlace(type).build()

            assertTrue(net.containsPlace(type))
        }

    }

    @Nested
    inner class AddTransition {

        @Test
        fun `is idempotent`() {
            `is idempotent`(signature = signature) { addTransition(it) }
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            `resulting Petri net contains the transition`(
                signature = signature,
                add = PNBuilder::addTransition,
                contains = FrontendPN::containsTransition
            )
        }

        @Test
        fun `throws if a type is missing`() {
            `throws if a type is missing`(signature, PNBuilder::addTransition)
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
            `resulting Petri net contains the transition`(mockk(), PNBuilder::addTransition, FrontendPN::containsTransition)
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
            `resulting Petri net contains the transition`(mockk(), PNBuilder::addTransition, FrontendPN::containsTransition)
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
            `resulting Petri net contains the transition`(mockk(), PNBuilder::addTransition,  FrontendPN::containsTransition)
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

    private val type = object : Type {
        override fun isCastableTo(type: Type?): Boolean {
            TODO()
        }

        override fun name(): String {
            return "Any"
        }

        override fun equals(other: Any?): Boolean {
            return true
        }

        override fun hashCode(): Int {
            return name().hashCode()
        }
    }

    private val signature = object : MSignature {
        override fun returnType(): Type {
            return type;
        }

        override fun name(): String {
            return "Method"
        }

        override fun parametersTypes(): MutableList<Type> {
            return MutableList(size = 3) { type }
        }

        override fun equals(other: Any?): Boolean {
            return true
        }

        override fun hashCode(): Int {
            return 0
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

    private fun `is idempotent`(
        signature: MSignature,
        add: PNBuilder.(MSignature) -> PNBuilder
    ) {
        val builder = createBuilder()

        `add method types`(builder, signature)

        val net1 = builder.add(signature).build()
        val net2 = builder.add(signature).build()

        assertEquals(expected = net1, actual = net2)
    }

    private fun `add method types`(
        builder: PNBuilder,
        signature: MSignature
    ) {
        builder.addPlace(signature.returnType())
        for (type in signature.parametersTypes()) {
            builder.addPlace(type)
        }
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

    private fun `resulting Petri net contains the transition`(
        signature: MSignature,
        add: PNBuilder.(MSignature) -> PNBuilder,
        contains: FrontendPN.(MSignature) -> Boolean
    ) {
        val builder = createBuilder()

        `add method types`(builder, signature)
        val net = builder.add(signature).build()

        assertTrue(net.contains(signature))
    }

    private fun <T> `throws if a type is missing`(
        node: T,
        add: PNBuilder.(T) -> PNBuilder
    ) {
        val builder = createBuilder()

        val action = { builder.add(node) }

        assertThrows<NoSuchPlaceException> { action() }
    }

    private fun <T, U> `throws if a type is missing`(
        node1: T,
        node2: U,
        add: PNBuilder.(T, U) -> PNBuilder
    ) {
        val builder = createBuilder()

        val action = { builder.add(node1, node2) }

        assertThrows<NoSuchPlaceException> { action() }
    }
}
