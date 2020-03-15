package edu.cmu.sypet.oracle

import edu.cmu.sypet.learner.Query
import edu.cmu.sypet.learner.Response

data class Oracle(val compiler: Compiler, val runner: Runner) {
    fun answer(query: Query): Response {
        TODO()
    }
}