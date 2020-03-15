package edu.cmu.sypet.learner

import edu.cmu.sypet.learner.sketchSolver.Program
import edu.cmu.sypet.learner.sketchSolver.SketchSolver
import edu.cmu.sypet.learner.sketcher.Sketch
import edu.cmu.sypet.learner.sketcher.Sketcher

data class Query(val sketch: Sketch)

sealed class Response
object Yes : Response()
object No : Response()

data class Spec(val testCases: Collection<TestCase>)

interface TestCase

data class Learner(val sketcher: Sketcher, val sketchSolver: SketchSolver) {
    private var done: Boolean = false

    fun start(spec: Spec): Query {
        TODO()
    }

    fun learn(response: Response): Query {
        TODO()
    }

    fun isDone(): Boolean {
        TODO()
    }

    fun stop(): Program? {
        TODO()
    }
}