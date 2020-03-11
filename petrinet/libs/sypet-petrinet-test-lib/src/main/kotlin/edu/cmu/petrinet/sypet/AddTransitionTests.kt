package edu.cmu.petrinet.sypet

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

interface AbstractAddTransitionTests {
    val add: PetriNetBuilder.(MethodSignature) -> PetriNetBuilder

    fun `the resulting Petri net contains the transition`(
        builder: PetriNetBuilder,
        signature: MethodSignature
    )

    fun `the places and the transition are adjacent`(
        builder: PetriNetBuilder,
        signature: MethodSignature
    )

    fun `the arcs have the correct weights`(
        builder: PetriNetBuilder,
        signature: MethodSignature
    )

    fun `throws if a type is missing`(
        builder: PetriNetBuilder,
        signature: MethodSignature
    ): PetriNetBuildException
}

data class AddTransitionTests(
    override val add: PetriNetBuilder.(MethodSignature) -> PetriNetBuilder
) : AbstractAddTransitionTests {
    override fun `the resulting Petri net contains the transition`(
        builder: PetriNetBuilder,
        signature: MethodSignature
    ) {
        builder.addMethodTypes(signature)

        val net = builder.add(signature).build()

        assertTrue(net.contains(signature))
    }

    override fun `the places and the transition are adjacent`(
        builder: PetriNetBuilder,
        signature: MethodSignature
    ) {
        builder.addMethodTypes(signature)

        val net = builder.add(signature).build()

        signature.parametersTypes().map { net.containsArc(it, signature) }
        assertTrue(net.containsArc(signature, signature.returnType()))
    }

    override fun `the arcs have the correct weights`(
        builder: PetriNetBuilder,
        signature: MethodSignature
    ) {
        builder.addMethodTypes(signature)

        val net = builder.add(signature).build()

        // Each parameter increments by one the weight of the arc between its type and the
        // method signature.
        signature.parametersTypes().toSet().map { type ->
            assertEquals(
                expected = signature.parametersTypes().count { it == type },
                actual = net.getArcWeight(type, signature)
            )
        }

        // Java methods can return at most one value.
        assertEquals(
            expected = 1,
            actual = net.getArcWeight(signature, signature.returnType())
        )
    }

    override fun `throws if a type is missing`(
        builder: PetriNetBuilder,
        signature: MethodSignature
    ) = assertFailsWith<PetriNetBuildException> { builder.add(signature) }
}

// TODO
//data class AddVoidTransitionTests(
//    val voidType: Type
//) : AbstractAddTransitionTests by AddTransitionTests(add = { sig ->
//    addVoidTransition(
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
//        add = PetriNetBuilder::addCloneTransition
//    )

// TODO
//    fun  `the places and the transition are adjacent`(
//        builder: PetriNetBuilder,
//        type: Type
//    ) = `the places and the transition are adjacent`(
//        builder = builder,
//        signature = CloneMethodSignature(type),
//        add = { addCloneTransition(type)}
//    )

// TODO
//    fun  `the arcs have the correct weights`(
//        builder: PetriNetBuilder,
//        signature: MethodSignature
//    ) = `the arcs have the correct weights`(
//        builder = builder,
//        signature = signature,
//        add = PetriNetBuilder::addCloneTransition
//    )

// TODO
//    fun  `throws if a type is missing`(
//        builder: PetriNetBuilder,
//        type: Type
//    ) = assertFailsWith<PetriNetBuildException> { builder.addCloneTransition(type) }
}

object AddCastTransition {
// TODO
//    fun  `the resulting Petri net contains the transition`(
//        builder: PetriNetBuilder,
//        signature: MethodSignature
//    ) = `the resulting Petri net contains the transition`(
//        builder = builder,
//        signature = signature,
//        add = PetriNetBuilder::addCastTransition
//    )

// TODO
//    fun  `the places and the transition are adjacent`(
//        builder: PetriNetBuilder,
//        signature: MethodSignature
//    ) = `the places and the transition are adjacent`(
//        builder = builder,
//        signature = signature,
//        add = PetriNetBuilder::addCastTransition
//    )

// TODO
//    fun  `the arcs have the correct weights`(
//        builder: PetriNetBuilder,
//        signature: MethodSignature
//    ) = `the arcs have the correct weights`(
//        builder = builder,
//        signature = signature,
//        add = PetriNetBuilder::addCastTransition
//    )

// TODO
//    fun  `throws if a type is missing`(
//        builder: PetriNetBuilder,
//        source: Type,
//        target: Type
//    ) = assertFailsWith<PetriNetBuildException> { builder.addCastTransition(source, target) }

}