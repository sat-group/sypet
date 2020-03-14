package edu.cmu.petrinet.apt

import edu.cmu.petrinet.sypet.*
import uniol.apt.adt.pn.PetriNet

data class SyPetBackendAdapter(val net: PetriNet) : BackendPetriNet {
    override fun add(node: BackendNode) {
        when (node) {
            is BackendPlace -> add(node)
            is BackendTransition -> add(node)
        }.let {}
    }

    internal fun add(place: BackendPlace) {
        requireNotContains(place)

        this.net.createPlace(place.id)
    }

    internal fun add(transition: BackendTransition) {
        requireNotContains(transition)

        this.net.createTransition(transition.id)
    }

    override fun add(arc: BackendArc) {
        when (arc) {
            is BackendInputArc -> add(arc)
            is BackendOutputArc -> add(arc)
        }
    }

    internal fun add(arc: BackendInputArc) {
        requireContains(arc.from)
        requireContains(arc.to)
        requireNotContains(arc)

        this.net.createFlow(arc.from.id, arc.to.id)
    }

    internal fun add(arc: BackendOutputArc) {
        requireContains(arc.from)
        requireContains(arc.to)
        requireNotContains(arc)

        this.net.createFlow(arc.from.id, arc.to.id, arc.weight!!)
    }

    override fun contains(node: BackendNode): Boolean {
        return when (node) {
            is BackendPlace -> contains(node)
            is BackendTransition -> contains(node)
        }
    }

    internal fun contains(place: BackendPlace): Boolean {
        return this.net.containsPlace(place.id)
    }

    internal fun contains(transition: BackendTransition): Boolean {
        return this.net.containsTransition(transition.id)
    }

    override operator fun contains(arc: BackendArc): Boolean {
        return when(arc) {
            is BackendInputArc -> contains(arc)
            is BackendOutputArc -> contains(arc)
        }
    }

    internal fun contains(arc: BackendInputArc): Boolean {
        requireContains(arc.from)
        requireContains(arc.to)

        return this.net.getPlace(arc.from.id).postset.any { it.id == arc.to.id }
    }

    internal fun contains(arc: BackendOutputArc): Boolean {
        requireContains(arc.from)
        requireContains(arc.to)

        return this.net.getTransition(arc.from.id).postset.any { it.id == arc.to.id }
    }

    private fun requireContains(node: BackendNode) {
        when (node) {
            is BackendPlace ->
                if (!this.net.containsPlace(node.id)) throw NoSuchNodeException(node)
                else return
            is BackendTransition ->
                if (!this.net.containsTransition(node.id)) throw NoSuchNodeException(node)
                else return
        }.let {}
    }

    private fun requireNotContains(node: BackendNode) {
        if (this.net.containsPlace(node.id)) {
            throw NodeAlreadyExistsException(node)
        }
    }

    private fun requireNotContains(arc: BackendArc) {
        if (this.contains(arc)) {
            throw ArcAlreadyExistsException(arc)
        }
    }
}