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
): MethodTransition {
    return object : MethodTransition {
        override fun id() = id
        override fun parametersTypes() = parametersTypes
        override fun returnType() = returnType
    }
}

class MethodSignatureGen : Gen<MethodTransition> {
    override fun constants() = listOf(
        newMockMethodSignature(parametersTypes = emptyList())
    )

    override fun random(): Sequence<MethodTransition> = generateSequence {
        newMockMethodSignature()
    }
}

private fun newMockBackendTransition(transition: MethodTransition): BackendTransition {
    val transition = mockk<BackendTransition>()

    every { transition.id() } returns transition.id()

    return transition
}

private val backendTransitionGen =
    MethodSignatureGen().random().map { newMockBackendTransition(it) }

class AddTransitionTests : AnnotationSpec() {

    @Test
    fun `the net contains the transition`() {
        MethodSignatureGen().forAll { transition: MethodTransition ->
            val backend = mockk<BackendPetriNet>(relaxed = true)
            val slot = slot<BackendTransition>()

            every { backend.addNode(capture(slot)) } just Runs

            val net = PetriNetBuilder(backend)
                .add(transition)
                .build()

            every { backend.containsNode(slot.captured) } returns true
            every { backend.containsNode(neq(slot.captured)) } returns false

            net.contains(transition)
        }
    }

    @Test
    fun `the places and the transition are adjacent`() {
        MethodSignatureGen().forAll { transition: MethodTransition ->
            val backend = mockk<BackendPetriNet>(relaxed = true)
            val transitionSlot = slot<BackendTransition>()
            val placeSlots = mutableListOf<BackendPlace>()

            every { backend.addNode(capture(transitionSlot)) } just Runs
            every { backend.addNode(capture(placeSlots)) } just Runs

            val net = PetriNetBuilder(backend)
                .add(transition.parametersTypes())
                .add(transition.returnType())
                .add(transition)
                .build()

            placeSlots.filter { it.id() == transition.returnType().id() }.map {
                every { backend.containsArc(transitionSlot.captured, it) } returns true
            }

            placeSlots.filter { placeSlot ->
                transition.parametersTypes().any { type -> type.id() == placeSlot.id() }
            }.map {
                every { backend.containsArc(it, transitionSlot.captured) } returns true
            }

            transition.parametersTypes().all { net.containsArc(it, transition) } &&
                    net.containsArc(transition, transition.returnType())
        }
    }
}