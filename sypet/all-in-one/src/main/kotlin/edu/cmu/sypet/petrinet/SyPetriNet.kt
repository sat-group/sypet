package edu.cmu.sypet.petrinet

import edu.cmu.sypet.lang.Library

data class SyPetriNet(private val net: PetriNet, val library: Library) {
    init {
        // build the net...
    }
}