package edu.cmu.petrinet.sypet

import kotlin.test.assertTrue

fun `the resulting Petri net contains the place`(builder: PetriNetBuilder, type: Type) =
    assertTrue(builder.add(type).build().contains(type))