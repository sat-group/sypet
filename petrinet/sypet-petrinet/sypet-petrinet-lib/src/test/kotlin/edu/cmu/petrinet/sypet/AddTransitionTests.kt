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

            every { type.getId() } returns idGen.random().first()

            type
        }
    }
}

val paramsGen = Gen.list(gen = TypeGen(), maxSize = 5)

private data class MockMethodSignature(
    private val id: String = idGen.random().first(),
    private val parametersTypes: Collection<Type> = paramsGen.random().first(),
    private val returnType: Type = TypeGen().random().first()
): MethodTransition {
    override fun getId() = id
    override fun parametersTypes() = parametersTypes
    override fun returnType() = returnType
}

private class MethodSignatureGen : Gen<MethodTransition> {
    override fun constants() = listOf(
        MockMethodSignature(parametersTypes = emptyList())
    )

    override fun random(): Sequence<MethodTransition> = generateSequence {
        MockMethodSignature()
    }
}

class AddTransitionTests : AnnotationSpec() {

    @Test
    fun `the net contains the transition`() {
        MethodSignatureGen().forAll { transition: MethodTransition ->
            val backend = mockk<BackendPetriNet>(relaxed = true)
            val slot = slot<BackendTransition>()

            every { backend.add(capture(slot)) } just Runs

            val net = PetriNetBuilder(backend)
                .add(transition)
                .build()

            every { backend.contains(slot.captured) } returns true
            every { backend.contains(neq(slot.captured)) } returns false

            net.contains(transition)
        }
    }

    @Test
    fun `the places and the transition are adjacent`() {
        MethodSignatureGen().forAll { transition: MethodTransition ->
            val backend = mockk<BackendPetriNet>(relaxed = true)
            val transitionSlot = slot<BackendTransition>()
            val placeSlots = mutableListOf<BackendPlace>()

            every { backend.add(capture(transitionSlot)) } just Runs
            every { backend.add(capture(placeSlots)) } just Runs

            val net = PetriNetBuilder(backend)
                .add(transition.parametersTypes())
                .add(transition.returnType())
                .add(transition)
                .build()

            placeSlots.filter { it.id == transition.returnType().id }.forEach {
                val outputArc = mockk<BackendOutputArc>()

                every { outputArc.from } returns transitionSlot.captured
                every { outputArc.to } returns it
                every { backend.contains(outputArc) } returns true
            }

            placeSlots.filter { transition.parametersTypes().any { type -> type.id == it.id } }
                .forEach {
                    val inputArc = mockk<BackendInputArc>()

                    every { inputArc.from } returns it
                    every { inputArc.to } returns transitionSlot.captured
                    every { backend.contains(inputArc) } returns true
                }

            transition.parametersTypes().all { net.containsArc(it, transition) } &&
                    net.containsArc(transition, transition.returnType())
        }
    }
}