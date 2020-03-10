package edu.cmu.petrinet.sypet

import kotlin.test.assertTrue

class AddPlaceTests<T, U> {
    fun `the resulting Petri net contains the place`(
        builder: PetriNetBuilder<T, U>,
        type: Type<T>
    ) = assertTrue(builder.addPlace(type).build().contains(type))
}

