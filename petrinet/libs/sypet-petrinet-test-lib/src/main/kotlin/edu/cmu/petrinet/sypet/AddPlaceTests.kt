package edu.cmu.petrinet.sypet

import kotlin.test.assertTrue

class AddPlaceTests {
    fun `the resulting Petri net contains the place`(builder: PetriNetBuilder, type: Type) =
        assertTrue(builder.addPlace(type).build().contains(type))
}

