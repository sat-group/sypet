package edu.cmu.petrinet.sypet

import edu.cmu.petrinet.apt.SyPetBackendAdapter
import org.junit.jupiter.api.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PetriNetBuilderTests {

    @Nested
    inner class AddPlace {
        @TestFactory
        fun `the resulting Petri net contains the place`() = generate(
            types,
            displayName = "`the resulting Petri net contains the place`"
        ) { type ->
            val builder = createBuilder()

            val net = builder.addPlace(type).build()

            assertTrue(net.containsPlace(type))
        }
    }

    @Nested
    inner class AddTransition {
        @TestFactory
        fun `the resulting Petri net contains the transition`() = generate(
            signatures,
            displayName = "`the resulting Petri net contains the transition`"
        ) {
            `resulting Petri net contains the transition`(
                signature = it,
                add = PetriNetBuilder::addTransition,
                contains = SyPetriNet<Type, MethodSignature>::containsTransition
            )
        }

        @TestFactory
        fun `the places and the transition are adjacent`() = generate(
            signatures,
            displayName = "`the places and the transition are adjacent`"
        ) {
            `the places and the transition are adjacent`(
                signature = it,
                add = PetriNetBuilder::addTransition
            )
        }

        @Test
        fun `the arcs have the correct weights`() = generate(
            signatures,
            displayName = "`the arcs have the correct weights`"
        ) { signature ->
            val builder = createBuilder()

            `add method types`(builder, signature)
            val net = builder.addTransition(signature).build()

            // Each parameter increments by one the weight of the arc between its type and the
            // method signature.
            signature.parametersTypes().toSet().map { type ->
                assertEquals(
                    expected = signature.parametersTypes().count { it == type },
                    actual = net.getArcWeightFromTypeToSignature(type, signature)
                )
            }

            // Java methods can return at most one value.
            assertEquals(
                expected = 1,
                actual = net.getArcWeightFromSignatureToType(signature, signature.returnType())
            )
        }

        @TestFactory
        fun `throws if a type is missing`() = generate(
            signatures,
            displayName = "`throws if a type is missing`"
        ) { signature ->
            `throws if a type is missing`(node = signature, add = PetriNetBuilder::addTransition)
        }
    }

    @Nested
    inner class AddVoidTransition {
        @TestFactory
        fun `the resulting Petri net contains the transition`() = generate(
            signatures,
            displayName = "`the resulting Petri net contains the transition`"
        ) {
            `resulting Petri net contains the transition`(
                signature = it,
                add = PetriNetBuilder::addTransition,
                contains = SyPetriNet<Type, MethodSignature>::containsTransition
            )
        }

        @TestFactory
        fun `throws if a type is missing`() =
            generate(signatures, displayName = "`throws if types are missing`") { signature ->
                `throws if a type is missing`(signature, PetriNetBuilder::addVoidTransition)
        }
    }

    @Nested
    inner class AddCloneTransition {
        @TestFactory
        fun `the resulting Petri net contains the transition`() = generate(
            signatures,
            displayName = "`the resulting Petri net contains the transition`"
        ) {
            `resulting Petri net contains the transition`(
                signature = it,
                add = PetriNetBuilder::addTransition,
                contains = SyPetriNet<Type, MethodSignature>::containsTransition
            )
        }

        @TestFactory
        fun `throws if a type is missing`() = generate(
            types,
            displayName = "`throws if a type is missing`"
        ) { type ->
            `throws if a type is missing`(type, PetriNetBuilder::addCloneTransition)
        }
    }

    @Nested
    inner class AddCastTransition {
        private val testTypes = types.zip(types.reversed())

        @TestFactory
        fun `the resulting Petri net contains the transition`() = generate(
            signatures,
            displayName = "`the resulting Petri net contains the transition`"
        ) {
            `resulting Petri net contains the transition`(
                signature = it,
                add = PetriNetBuilder::addTransition,
                contains = SyPetriNet<Type, MethodSignature>::containsTransition
            )
        }

        @TestFactory
        fun `throws if a type is missing`() = generate(
            testTypes.filter { (type1, type2) -> type1 != type2 },
            displayName = "`throws if a type is missing`"
        ) { (type1, type2) ->

            val builder = createBuilder()
            builder.addPlace(type1)

            `throws if a type is missing`(type1, type2, PetriNetBuilder::addCastTransition, builder)
            `throws if a type is missing`(type2, type1, PetriNetBuilder::addCastTransition, builder)
        }

    }
}

private fun createBuilder(): PetriNetBuilder {
    return PetriNetBuilder(SyPetBackendAdapter())
}

private fun <T> generate(cases: List<T>, displayName: String, test: (T) -> Unit) = cases.map {
    DynamicTest.dynamicTest(displayName) { test(it) }
}

private fun `add method types`(
    builder: PetriNetBuilder,
    signature: MethodSignature
) {
    builder.addPlace(signature.returnType())
    for (type in signature.parametersTypes().toSet()) {
        builder.addPlace(type)
    }
}

private fun `resulting Petri net contains the transition`(
    signature: MethodSignature,
    add: PetriNetBuilder.(MethodSignature) -> PetriNetBuilder,
    contains: SyPetriNet<Type, MethodSignature>.(MethodSignature) -> Boolean
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
    add: PetriNetBuilder.(T, U) -> PetriNetBuilder,
    builder: PetriNetBuilder
) {
    val action = { builder.add(node1, node2) }

    assertThrows<NoSuchPlaceException> { action() }
}

private fun `the places and the transition are adjacent`(
    signature: MethodSignature,
    add: PetriNetBuilder.(MethodSignature) -> PetriNetBuilder
) {
    val builder = createBuilder()

    `add method types`(builder = builder, signature = signature)

    val net = builder.add(signature).build()

    signature.parametersTypes().map { net.isTypeAdjacentToSignature(it, signature) }
    assertTrue(net.isSignatureAdjacentToType(signature, signature.returnType()))
}
