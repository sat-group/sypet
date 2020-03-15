package edu.cmu.sypet.learner.sketcher

import edu.cmu.sypet.petrinet.SyPetriNet

interface Sketch

data class Sketcher(val net: SyPetriNet) {
    val sketches: Sequence<Sketch> = TODO()
}

