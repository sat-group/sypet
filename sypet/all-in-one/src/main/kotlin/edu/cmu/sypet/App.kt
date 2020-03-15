package edu.cmu.sypet

import edu.cmu.sypet.learner.Spec
import edu.cmu.sypet.learner.sketchSolver.Program
import edu.cmu.sypet.synthesiser.Synthesiser
import edu.cmu.sypet.synthesiser.solve

data class App(val io: IO, val locator: ServiceLocator)

interface IO {
    fun getSpec(): Spec
    fun putOutput(output: Program?)
}

fun App.run() {
    val synthesiser = locator.get<Synthesiser>()
    val spec = io.getSpec()
    val output = synthesiser.solve(spec)

    io.putOutput(output)
}

val defaultApp: App = TODO()

fun main(args: Array<String>) {
    defaultApp.run()
}