package edu.cmu.petrinet.sypet

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

interface AbstractAddTransitionTests<T, U> {
    val add: PetriNetBuilder<T, U>.(MethodSignature<T, U>) -> PetriNetBuilder<T, U>

    fun `the resulting Petri net contains the transition`(
        builder: PetriNetBuilder<T, U>,
        signature: MethodSignature<T, U>
    )

    fun `the places and the transition are adjacent`(
        builder: PetriNetBuilder<T, U>,
        signature: MethodSignature<T, U>
    )

    fun `the arcs have the correct weights`(
        builder: PetriNetBuilder<T, U>,
        signature: MethodSignature<T, U>
    )

    fun `throws if a type is missing`(
        builder: PetriNetBuilder<T, U>,
        signature: MethodSignature<T, U>
    ): PetriNetBuildException
}

data class AddTransitionTests<T, U>(
    override val add: PetriNetBuilder<T, U>.(MethodSignature<T, U>) -> PetriNetBuilder<T, U>
) : AbstractAddTransitionTests<T, U> {
    override fun `the resulting Petri net contains the transition`(
        builder: PetriNetBuilder<T, U>,
        signature: MethodSignature<T, U>
    ) {
        builder.addMethodTypes(signature)

        val net = builder.add(signature).build()

        assertTrue(net.contains(signature))
    }

    override fun `the places and the transition are adjacent`(
        builder: PetriNetBuilder<T, U>,
        signature: MethodSignature<T, U>
    ) {
        builder.addMethodTypes(signature)

        val net = builder.add(signature).build()

        signature.parametersTypes().map { net.containsArc(it, signature) }
        assertTrue(net.containsArc(signature, signature.returnType()))
    }

    override fun `the arcs have the correct weights`(
        builder: PetriNetBuilder<T, U>,
        signature: MethodSignature<T, U>
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
        builder: PetriNetBuilder<T, U>,
        signature: MethodSignature<T, U>
    ) = assertFailsWith<PetriNetBuildException> { builder.add(signature) }
}

data class AddVoidTransitionTests<T, U>(
    val voidType: Type<T>
) : AbstractAddTransitionTests<T, U> by AddTransitionTests<T, U>(add = { sig ->
    addVoidTransition(
        sig,
        voidType
    )
})

object AddCloneTransition {
// TODO
//    fun <T, U> `the resulting Petri net contains the transition`(
//        builder: PetriNetBuilder<T, U>,
//        signature: MethodSignature<T, U>
//    ) = `the resulting Petri net contains the transition`(
//        builder = builder,
//        signature = signature,
//        add = PetriNetBuilder<T, U>::addCloneTransition
//    )

// TODO
//    fun <T, U> `the places and the transition are adjacent`(
//        builder: PetriNetBuilder<T, U>,
//        type: Type<T>
//    ) = `the places and the transition are adjacent`(
//        builder = builder,
//        signature = CloneMethodSignature(type),
//        add = { addCloneTransition(type)}
//    )

// TODO
//    fun <T, U> `the arcs have the correct weights`(
//        builder: PetriNetBuilder<T, U>,
//        signature: MethodSignature<T, U>
//    ) = `the arcs have the correct weights`(
//        builder = builder,
//        signature = signature,
//        add = PetriNetBuilder<T, U>::addCloneTransition
//    )

    fun <T, U> `throws if a type is missing`(
        builder: PetriNetBuilder<T, U>,
        type: Type<T>
    ) = assertFailsWith<PetriNetBuildException> { builder.addCloneTransition(type) }
}

object AddCastTransition {
// TODO
//    fun <T, U> `the resulting Petri net contains the transition`(
//        builder: PetriNetBuilder<T, U>,
//        signature: MethodSignature<T, U>
//    ) = `the resulting Petri net contains the transition`(
//        builder = builder,
//        signature = signature,
//        add = PetriNetBuilder<T, U>::addCastTransition
//    )

// TODO
//    fun <T, U> `the places and the transition are adjacent`(
//        builder: PetriNetBuilder<T, U>,
//        signature: MethodSignature<T, U>
//    ) = `the places and the transition are adjacent`(
//        builder = builder,
//        signature = signature,
//        add = PetriNetBuilder<T, U>::addCastTransition
//    )

// TODO
//    fun <T, U> `the arcs have the correct weights`(
//        builder: PetriNetBuilder<T, U>,
//        signature: MethodSignature<T, U>
//    ) = `the arcs have the correct weights`(
//        builder = builder,
//        signature = signature,
//        add = PetriNetBuilder<T, U>::addCastTransition
//    )

    fun <T, U> `throws if a type is missing`(
        builder: PetriNetBuilder<T, U>,
        source: Type<T>,
        target: Type<T>
    ) = assertFailsWith<PetriNetBuildException> { builder.addCastTransition(source, target) }

}