package edu.cmu.petrinet.sypet

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

interface AbstractAddTransitionTests {
    val add: PetriNetBuilder.(MethodTransition) -> PetriNetBuilder

    fun `the resulting Petri net contains the transition`(
        builder: PetriNetBuilder,
        transition: MethodTransition
    )

    fun `the places and the transition are adjacent`(
        builder: PetriNetBuilder,
        transition: MethodTransition
    )

    fun `the arcs have the correct weights`(
        builder: PetriNetBuilder,
        transition: MethodTransition
    )

    fun `throws if a type is missing`(
        builder: PetriNetBuilder,
        transition: MethodTransition
    ): PetriNetBuildException
}

data class AddTransitionTests(
    override val add: PetriNetBuilder.(MethodTransition) -> PetriNetBuilder
) : AbstractAddTransitionTests {
    override fun `the resulting Petri net contains the transition`(
        builder: PetriNetBuilder,
        transition: MethodTransition
    ) {
        builder.addMethodTypes(transition)

        val net = builder.add(transition).build()

        assertTrue(net.contains(transition))
    }

    override fun `the places and the transition are adjacent`(
        builder: PetriNetBuilder,
        transition: MethodTransition
    ) {
        builder.addMethodTypes(transition)

        val net = builder.add(transition).build()

        transition.parametersTypes().map { net.containsArc(it, transition) }
        assertTrue(net.containsArc(transition, transition.returnType()))
    }

    override fun `the arcs have the correct weights`(
        builder: PetriNetBuilder,
        transition: MethodTransition
    ) {
        builder.addMethodTypes(transition)

        val net = builder.add(transition).build()

        // Each parameter increments by one the weight of the arc between its type and the
        // method signature.
        transition.parametersTypes().toSet().map { type ->
            assertEquals(
                expected = transition.parametersTypes().count { it == type },
                actual = net.getArcWeight(type, transition)
            )
        }

        // Java methods can return at most one value.
        assertEquals(
            expected = 1,
            actual = net.getArcWeight(transition, transition.returnType())
        )
    }

    override fun `throws if a type is missing`(
        builder: PetriNetBuilder,
        transition: MethodTransition
    ) = assertFailsWith<PetriNetBuildException> { builder.add(transition) }
}

// TODO
//data class AddVoidTransitionTests(
//    val voidType: Type
//) : AbstractAddTransitionTests by AddTransitionTests(add = { sig ->
//    add(
//        sig,
//        voidType
//    )
//})

object AddCloneTransition {
// TODO
//    fun  `the resulting Petri net contains the transition`(
//        builder: PetriNetBuilder,
//        signature: MethodSignature
//    ) = `the resulting Petri net contains the transition`(
//        builder = builder,
//        signature = signature,
//        add = PetriNetBuilder::add
//    )

// TODO
//    fun  `the places and the transition are adjacent`(
//        builder: PetriNetBuilder,
//        type: Type
//    ) = `the places and the transition are adjacent`(
//        builder = builder,
//        signature = CloneMethodSignature(type),
//        add = { add(type)}
//    )

// TODO
//    fun  `the arcs have the correct weights`(
//        builder: PetriNetBuilder,
//        signature: MethodSignature
//    ) = `the arcs have the correct weights`(
//        builder = builder,
//        signature = signature,
//        add = PetriNetBuilder::add
//    )

// TODO
//    fun  `throws if a type is missing`(
//        builder: PetriNetBuilder,
//        type: Type
//    ) = assertFailsWith<PetriNetBuildException> { builder.add(type) }
}

object AddCastTransition {
// TODO
//    fun  `the resulting Petri net contains the transition`(
//        builder: PetriNetBuilder,
//        signature: MethodSignature
//    ) = `the resulting Petri net contains the transition`(
//        builder = builder,
//        signature = signature,
//        add = PetriNetBuilder::add
//    )

// TODO
//    fun  `the places and the transition are adjacent`(
//        builder: PetriNetBuilder,
//        signature: MethodSignature
//    ) = `the places and the transition are adjacent`(
//        builder = builder,
//        signature = signature,
//        add = PetriNetBuilder::add
//    )

// TODO
//    fun  `the arcs have the correct weights`(
//        builder: PetriNetBuilder,
//        signature: MethodSignature
//    ) = `the arcs have the correct weights`(
//        builder = builder,
//        signature = signature,
//        add = PetriNetBuilder::add
//    )

// TODO
//    fun  `throws if a type is missing`(
//        builder: PetriNetBuilder,
//        source: Type,
//        target: Type
//    ) = assertFailsWith<PetriNetBuildException> { builder.add(source, target) }

}