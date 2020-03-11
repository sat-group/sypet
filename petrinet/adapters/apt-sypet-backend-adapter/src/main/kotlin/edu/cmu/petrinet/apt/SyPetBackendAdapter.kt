package edu.cmu.petrinet.apt

import edu.cmu.petrinet.sypet.*
import uniol.apt.adt.pn.PetriNet

data class SyPetBackendAdapter(val net: PetriNet) : BackendPetriNet {
    override fun addNode(place: BackendPlace?) {
        requireNotNull(place)
        requireNotContainsPlace(place)

        this.net.createPlace(place.id())
    }

    override fun addNode(transition: BackendTransition?) {
        requireNotNull(transition)
        requireNotContainsTransition(transition)

        this.net.createTransition(transition.id())
    }

    override fun addArc(place: BackendPlace?, transition: BackendTransition?, weight: Int?) {
        requireNotNull(place)
        requireNotNull(transition)
        requireNotNull(weight)
        requireContainsPlace(place)
        requireContainsTransition(transition)
        requireNotContainsArc(place, transition)

        this.net.createFlow(place.id(), transition.id())
    }

    override fun addArc(transition: BackendTransition?, place: BackendPlace?, weight: Int?) {
        requireNotNull(transition)
        requireNotNull(place)
        requireNotNull(weight)
        requireContainsTransition(transition)
        requireContainsPlace(place)
        requireNotContainsArc(transition, place)

        this.net.createFlow(transition.id(), place.id(), weight)
    }

    override fun containsNode(place: BackendPlace?): Boolean {
        requireNotNull(place)
        return this.net.containsPlace(place.id())
    }

    override fun containsNode(transition: BackendTransition?): Boolean {
        requireNotNull(transition)
        return this.net.containsTransition(transition.id())
    }

    override fun containsArc(place: BackendPlace?, transition: BackendTransition?): Boolean {
        requireNotNull(place)
        requireNotNull(transition)
        requireContainsPlace(place)
        requireContainsTransition(transition)

        return this.net.getPlace(place.id()).postset.any { _transition ->
            _transition.id == transition.id()
        }
    }

    override fun containsArc(transition: BackendTransition?, place: BackendPlace?): Boolean {
        requireNotNull(transition)
        requireNotNull(place)
        requireContainsTransition(transition)
        requireContainsPlace(place)

        return this.net.getTransition(transition.id()).postset.any { _place ->
            _place.id == place.id()
        }
    }

    override fun getArcWeight(place: BackendPlace?, transition: BackendTransition?): Int {
        requireContainsArc(place, transition)
        return this.net.getFlow(place!!.id(), transition!!.id()).weight
    }

    override fun getArcWeight(transition: BackendTransition?, place: BackendPlace?): Int {
        requireContainsArc(transition, place)
        return this.net.getFlow(transition!!.id(), place!!.id()).weight
    }

    private fun requireContainsPlace(place: BackendPlace) {
        if (!this.net.containsPlace(place.id())) {
            throw NoSuchPlaceException(place)
        }
    }

    private fun requireNotContainsPlace(place: BackendPlace) {
        if (this.net.containsPlace(place.id())) {
            throw PlaceAlreadyExistsException(place)
        }
    }

    private fun requireContainsTransition(transition: BackendTransition) {
        if (!this.net.containsTransition(transition.id())) {
            throw NoSuchTransitionException(transition)
        }
    }

    private fun requireNotContainsTransition(transition: BackendTransition) {
        if (this.net.containsTransition(transition.id())) {
            throw TransitionAlreadyExistsException(transition)
        }
    }

    private fun requireNotContainsArc(place: BackendPlace, transition: BackendTransition) {
        if (this.containsArc(place, transition)) {
            throw ArcAlreadyExistsException(place, transition)
        }
    }

    private fun requireNotContainsArc(transition: BackendTransition, place: BackendPlace) {
        if (this.containsArc(transition, place)) {
            throw ArcAlreadyExistsException(transition, place)
        }
    }

    private fun requireContainsArc(place: BackendPlace?, transition: BackendTransition?) {
        if (!this.containsArc(place, transition)) {
            throw NoSuchArcException(place, transition)
        }
    }

    private fun requireContainsArc(transition: BackendTransition?, place: BackendPlace?) {
        if (!this.containsArc(transition, place)) {
            throw NoSuchArcException(transition, place)
        }
    }
}