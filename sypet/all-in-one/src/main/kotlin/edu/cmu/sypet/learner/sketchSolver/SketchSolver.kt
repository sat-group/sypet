package edu.cmu.sypet.learner.sketchSolver

import edu.cmu.sypet.learner.sketcher.Sketch

data class SketchSolver(val sketch: Sketch, val satSolver: SatSolver) {
    fun solve(sketch: Sketch): Program? {
        TODO()
    }
}
