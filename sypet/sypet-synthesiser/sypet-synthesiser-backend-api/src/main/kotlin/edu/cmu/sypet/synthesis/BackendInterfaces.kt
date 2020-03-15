package edu.cmu.sypet.synthesis

interface Query
interface Response

interface Learner {
    interface Specification
    interface Output

    fun start(spec: Specification): Query
    fun learn(response: Response): Query
    fun isDone(): Boolean
    fun stop(): Output
}

interface Oracle {
    fun answer(query: Query): Response
}