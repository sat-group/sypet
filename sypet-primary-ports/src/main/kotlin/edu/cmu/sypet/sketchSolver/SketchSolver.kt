package edu.cmu.sypet.sketchSolver

import edu.cmu.sypet.sketcher.Sketch

data class SketchSolver(val sketch: Sketch, val satSolver: SatSolver) {
    fun solve(sketch: Sketch): Program? {
        TODO()
    }
}
