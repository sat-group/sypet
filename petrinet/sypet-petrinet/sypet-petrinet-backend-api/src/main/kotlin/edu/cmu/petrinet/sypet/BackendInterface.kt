package edu.cmu.petrinet.sypet

interface BackendPetriNet {
    @Throws(NodeAlreadyExistsException::class)
    fun add(node: BackendNode)

    @Throws(ArcAlreadyExistsException::class, NoSuchNodeException::class)
    fun add(arc: BackendArc)

    operator fun contains(node: BackendNode): Boolean

    operator fun contains(arc: BackendArc): Boolean
}

sealed class BackendNode {
    abstract val id: String
}

abstract class BackendPlace : BackendNode()

abstract class BackendTransition : BackendNode()

sealed class BackendArc {
    abstract val from: BackendNode
    abstract val to: BackendNode
    abstract val weight: Int? // FIXME
}

abstract class BackendInputArc(
    override val from: BackendPlace,
    override val to: BackendTransition,
    override val weight: Int?
) : BackendArc()

abstract class BackendOutputArc(
    override val from: BackendTransition,
    override val to: BackendPlace,
    override val weight: Int?
) : BackendArc()