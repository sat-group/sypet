package edu.cmu.petrinet.sypet

data class ArcAlreadyExistsException(val arc: BackendArc) : Exception()

data class NoSuchNodeException(val node: BackendNode) : Exception()

data class NodeAlreadyExistsException(val node: BackendNode) : Exception()