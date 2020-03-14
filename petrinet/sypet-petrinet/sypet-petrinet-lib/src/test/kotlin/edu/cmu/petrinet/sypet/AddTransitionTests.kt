package edu.cmu.petrinet.sypet

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.AnnotationSpec
import io.mockk.*

val idGen = Gen.string()

class TypeGen : Gen<Type> {
    override fun constants() = emptyList<Type>()

    override fun random(): Sequence<Type> {
        return generateSequence {
            val type = mockk<Type>()

            every { type.id() } returns idGen.random().first()

            type
        }
    }
}

val paramsGen = Gen.list(gen = TypeGen(), maxSize = 5)

private fun newMockMethodSignature(
    id: String = idGen.random().first(),
    parametersTypes: Collection<Type> = paramsGen.random().first(),
    returnType: Type = TypeGen().random().first()
): MethodSignature {
    return object : MethodSignature {
        override fun id() = id
        override fun parametersTypes() = parametersTypes
        override fun returnType() = returnType
    }
}

class MethodSignatureGen : Gen<MethodSignature> {
    override fun constants() = listOf(
        newMockMethodSignature(parametersTypes = emptyList())
    )

    override fun random(): Sequence<MethodSignature> = generateSequence {
        newMockMethodSignature()
    }
}

private fun newMockBackendTransition(signature: MethodSignature): BackendTransition {
    val transition = mockk<BackendTransition>()

    every { transition.id() } returns signature.id()

    return transition
}

private val backendTransitionGen =
    MethodSignatureGen().random().map { newMockBackendTransition(it) }

class AddTransitionTests : AnnotationSpec() {

    @Test
    fun `the net contains the transition`() {
        MethodSignatureGen().forAll { signature: MethodSignature ->
            val backend = mockk<BackendPetriNet>(relaxed = true)
            val slot = slot<BackendTransition>()

            every { backend.addNode(capture(slot)) } just Runs

            val net = PetriNetBuilder(backend)
                .add(signature)
                .build()

            every { backend.containsNode(slot.captured) } returns true
            every { backend.containsNode(neq(slot.captured)) } returns false

            net.contains(signature)
        }
    }

    @Test
    fun `the places and the transition are adjacent`() {
        MethodSignatureGen().forAll { signature: MethodSignature ->
            val backend = mockk<BackendPetriNet>(relaxed = true)
            val transitionSlot = slot<BackendTransition>()
            val placeSlots = mutableListOf<BackendPlace>()

            every { backend.addNode(capture(transitionSlot)) } just Runs
            every { backend.addNode(capture(placeSlots)) } just Runs

            val net = PetriNetBuilder(backend)
                .add(signature.parametersTypes())
                .add(signature.returnType())
                .add(signature)
                .build()

            placeSlots.filter { it.id() == signature.returnType().id() }.map {
                every { backend.containsArc(transitionSlot.captured, it) } returns true
            }

            placeSlots.filter { placeSlot ->
                signature.parametersTypes().any { type -> type.id() == placeSlot.id() }
            }.map {
                every { backend.containsArc(it, transitionSlot.captured) } returns true
            }

            signature.parametersTypes().all { net.containsArc(it, signature) } &&
                    net.containsArc(signature, signature.returnType())
        }
    }
}