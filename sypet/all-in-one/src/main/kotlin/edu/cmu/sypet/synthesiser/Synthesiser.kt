package edu.cmu.sypet.synthesiser

import edu.cmu.sypet.learner.Learner
import edu.cmu.sypet.learner.Spec
import edu.cmu.sypet.learner.sketchSolver.Program
import edu.cmu.sypet.oracle.Oracle

data class Synthesiser(val learner: Learner, val oracle: Oracle)

internal fun Synthesiser.solve(spec: Spec): Program? {
    var query = learner.start(spec)

    while (!learner.isDone()) {
        val response = oracle.answer(query)
        query = learner.learn(response)
    }

    return learner.stop()
}
