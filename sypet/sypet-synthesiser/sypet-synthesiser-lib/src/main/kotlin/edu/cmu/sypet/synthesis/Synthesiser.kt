package edu.cmu.sypet.synthesis

data class Synthesiser(val learner: Learner, val oracle: Oracle)

fun Synthesiser.solve(spec: Specification): Output {
    val query = learner.start(SpecAdapter(spec))

    tailrec fun loop(query: Query): Learner.Output {
        return if (learner.isDone()) {
            learner.stop()
        } else {
            val response = oracle.answer(query)
            @Suppress("NAME_SHADOWING")
            val query = learner.learn(response)

            loop(query)
        }
    }

    return OutputAdapter(loop(query))
}

data class SpecAdapter(val spec: Specification): Learner.Specification
data class OutputAdapter(val output: Learner.Output): Output

