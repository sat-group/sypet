package edu.cmu.petrinet.sypet

import edu.cmu.petrinet.simple.PetriNetFactory
import edu.cmu.petrinet.simple.SimplePetriNet
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PetriNetBuilderTests {

    @Nested
    inner class AddPlace {

        @Test
        fun `is idempotent`() {
            `is idempotent`(DefaultType(), PetriNetBuilder::addPlace)
        }

        @Test
        fun `the resulting Petri net contains the place`() {
            val builder = createBuilder()

            val net = builder.addPlace(DefaultType()).build()

            assertTrue(net.containsPlace(DefaultType()))
        }

    }

    @Nested
    inner class AddTransition {

        @Test
        fun `is idempotent`() {
            `is idempotent`(signature = DefaultMethodSignature()) { addTransition(it) }
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            `resulting Petri net contains the transition`(
                signature = DefaultMethodSignature(),
                add = PetriNetBuilder::addTransition,
                contains = SyPetriNet::containsTransition
            )
        }

        @Test
        fun `throws if a type is missing`() {
            `throws if a type is missing`(DefaultMethodSignature(), PetriNetBuilder::addTransition)
        }

    }

    @Nested
    inner class AddVoidTransition {

        @Test
        fun `is idempotent`() {
            `is idempotent`(signature = DefaultMethodSignature()) { addVoidTransition(it) }
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            `resulting Petri net contains the transition`(
                DefaultMethodSignature(),
                PetriNetBuilder::addTransition,
                SyPetriNet::containsTransition
            )
        }

        @Test
        fun `throws if types are missing`() {
            `throws if a type is missing`(DefaultMethodSignature(),
                PetriNetBuilder::addVoidTransition)
        }

    }

    @Nested
    inner class AddCloneTransition {

        @Test
        fun `is idempotent`() {
            val builder = createBuilder()
            val type = DefaultType()

            builder.addPlace(type)
            val net1 = builder.addCloneTransition(type).build()
            val net2 = builder.addCloneTransition(type).build()

            assertEquals(expected = net1, actual = net2)
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            `resulting Petri net contains the transition`(
                DefaultMethodSignature(),
                PetriNetBuilder::addTransition,
                SyPetriNet::containsTransition
            )
        }

        @Test
        fun `throws if a type is missing`() {
            `throws if a type is missing`(DefaultType(), PetriNetBuilder::addCloneTransition)
        }

    }

    @Nested
    inner class AddCastTransition {

        @Test
        fun `is idempotent`() {
            val builder = createBuilder()

            val from = DefaultType(isCastableTo = true)
            val to = DefaultType()

            builder.addPlace(from).addPlace(to)
            val net1 = builder.addCastTransition(from, to).build()
            val net2 = builder.addCastTransition(from, to).build()

            assertEquals(expected = net1, actual = net2)
        }

        @Test
        fun `the resulting Petri net contains the transition`() {
            `resulting Petri net contains the transition`(
                DefaultMethodSignature(),
                PetriNetBuilder::addTransition,
                SyPetriNet::containsTransition
            )
        }

        @Test
        fun `throws if a type is missing`() {
            val from = DefaultType(isCastableTo = true)
            val to = DefaultType(isCastableTo = true)

            `throws if a type is missing`(from, to, PetriNetBuilder::addCastTransition)
        }

        @Test
        fun `throws if the types are not castable`() {
            val builder = createBuilder()

            val from = DefaultType()
            val to = DefaultType()

            builder
                .addPlace(from)
                .addPlace(to)

            assertThrows<BadCastException> { builder.addCastTransition(from, to) }
        }

    }

    private class DefaultType(private val isCastableTo: Boolean = false) : Type {
        override fun isCastableTo(type: Type?): Boolean {
            return isCastableTo
        }

        override fun name(): String {
            return "Any"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }

    }

    private class DefaultMethodSignature() : MethodSignature {
        override fun returnType(): Type {
            return DefaultType()
        }

        override fun name(): String {
            return "Method"
        }

        override fun parametersTypes(): MutableList<Type> {
            return MutableList(size = 3) { DefaultType() }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }

    }

    private class DefaultBackendPetriNet(val petriNet: SimplePetriNet<Type, MethodSignature>) :
        BackendPetriNet<Type, MethodSignature> {
        override fun containsPlace(type: Type): Boolean {
            return petriNet.containsPlace(type)
        }

        override fun containsTransition(signature: MethodSignature): Boolean {
            return petriNet.containsTransition(signature)
        }

        override fun addPlace(type: Type) {
            return petriNet.addPlace(type)
        }

        override fun addTransition(signature: MethodSignature) {
            return petriNet.addTransition(signature)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }

            val defaulPetriNet = other as DefaultBackendPetriNet
            return this.petriNet == defaulPetriNet.petriNet
        }

        override fun hashCode(): Int {
            return petriNet.hashCode()
        }
    }

    private fun createBuilder(): PetriNetBuilder {
        val simplePetriNet = PetriNetFactory().create<Type, MethodSignature>()
        return PetriNetBuilder(DefaultBackendPetriNet(simplePetriNet))
    }

    private fun <T> `is idempotent`(
        node: T,
        add: PetriNetBuilder.(T) -> PetriNetBuilder
    ) {
        val builder = createBuilder()

        val net1 = builder.add(node).build()
        val net2 = builder.add(node).build()

        assertEquals(expected = net1, actual = net2)
    }

    private fun `is idempotent`(
        signature: MethodSignature,
        add: PetriNetBuilder.(MethodSignature) -> PetriNetBuilder
    ) {
        val builder = createBuilder()

        `add method types`(builder, signature)

        val net1 = builder.add(signature).build()
        val net2 = builder.add(signature).build()

        assertEquals(expected = net1, actual = net2)
    }

    private fun `add method types`(
        builder: PetriNetBuilder,
        signature: MethodSignature
    ) {
        builder.addPlace(signature.returnType())
        for (type in signature.parametersTypes()) {
            builder.addPlace(type)
        }
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

    private fun `resulting Petri net contains the transition`(
        signature: MethodSignature,
        add: PetriNetBuilder.(MethodSignature) -> PetriNetBuilder,
        contains: SyPetriNet.(MethodSignature) -> Boolean
    ) {
        val builder = createBuilder()

        `add method types`(builder, signature)
        val net = builder.add(signature).build()

        assertTrue(net.contains(signature))
    }

    private fun <T> `throws if a type is missing`(
        node: T,
        add: PetriNetBuilder.(T) -> PetriNetBuilder
    ) {
        val builder = createBuilder()

        val action = { builder.add(node) }

        assertThrows<NoSuchPlaceException> { action() }
    }

    private fun <T, U> `throws if a type is missing`(
        node1: T,
        node2: U,
        add: PetriNetBuilder.(T, U) -> PetriNetBuilder
    ) {
        val builder = createBuilder()

        val action = { builder.add(node1, node2) }

        assertThrows<NoSuchPlaceException> { action() }
    }
}
