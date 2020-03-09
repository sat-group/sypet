package edu.cmu.petrinet.apt

import edu.cmu.petrinet.sypet.BackendPetriNet
import edu.cmu.petrinet.sypet.MethodSignature
import edu.cmu.petrinet.sypet.Type
import uniol.apt.adt.pn.Flow
import uniol.apt.adt.pn.PetriNet
import uniol.apt.adt.pn.Place
import uniol.apt.adt.pn.Transition

private val Type.id
    get() = hashCode().toString()

private val MethodSignature.id
    get() = hashCode().toString()

class SyPetBackendAdapter : BackendPetriNet<Type, MethodSignature> {
    private val net: PetriNet = PetriNet()

    override fun addPlace(type: Type?) {
        requireNotNull(type)
        this.net.createPlace(type.id)
    }

    override fun addTransition(signature: MethodSignature?) {
        requireNotNull(signature)
        this.net.createTransition(signature.id)
    }

    override fun addArcFromPlaceToTransition(
        type: Type?,
        signature: MethodSignature?,
        weight: Int?
    ) {
        requireNotNull(type)
        requireNotNull(signature)
        requireNotNull(weight)
        require(!this.isPlaceAdjacentToTransition(type, signature))

        this.net.createFlow(type.id, signature.id, weight)
    }

    override fun addArcFromTransitionToPlace(
        signature: MethodSignature?,
        type: Type?,
        weight: Int?
    ) {
        requireNotNull(signature)
        requireNotNull(type)
        requireNotNull(weight)
        require(!this.isTransitionAdjacentToPlace(signature, type))

        this.net.createFlow(signature.id, type.id, weight)
    }

    override fun containsPlace(type: Type?): Boolean {
        requireNotNull(type)
        return this.net.containsPlace(type.id)
    }

    override fun containsTransition(signature: MethodSignature?): Boolean {
        requireNotNull(signature)
        return this.net.containsTransition(signature.id)
    }

    override fun isPlaceAdjacentToTransition(type: Type?, signature: MethodSignature?): Boolean {
        requireNotNull(type)
        requireNotNull(signature)
        require(this.net.containsPlace(type.id))
        require(this.net.containsTransition(signature.id))

        return this.net.getPlace(type.id).postset.any { transition ->
            transition.id == signature.id
        }
    }

    override fun isTransitionAdjacentToPlace(signature: MethodSignature?, type: Type?): Boolean {
        requireNotNull(signature)
        requireNotNull(type)
        require(this.net.containsTransition(signature.id))
        require(this.net.containsPlace(type.id))

        return this.net.getTransition(signature.id).postset.any { place ->
            place.id == type.id
        }
    }

    override fun getArcWeightFromTypeToSignature(type: Type?, signature: MethodSignature?): Int {
        require(this.isPlaceAdjacentToTransition(type, signature))
        return this.net.getFlow(type!!.id, signature!!.id).weight
    }

    override fun getArcWeightFromSignatureToType(signature: MethodSignature?, type: Type?): Int {
        require(isTransitionAdjacentToPlace(signature, type))
        return this.net.getFlow(signature!!.id, type!!.id).weight
    }

    private val places
        get() = net.places.map(::PlaceWrapper).toSet()
    private val transitions
        get() = net.transitions.map(::TransitionWrapper).toSet()
    private val flows
        get() = net.edges.map(::FlowWrapper).toSet()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SyPetBackendAdapter

        if (this.places != other.places) return false
        if (this.transitions != other.transitions) return false
        if (this.flows != other.flows) return false

        return true
    }

    override fun hashCode(): Int {
        return net.hashCode()
    }
}

data class PlaceWrapper(val place: Place) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaceWrapper

        // Comparison by id.
        if (place.id != other.place.id) return false

        return true
    }

    override fun hashCode(): Int {
        return place.id.hashCode()
    }
}

data class TransitionWrapper(val transition: Transition) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TransitionWrapper

        // Comparison by id.
        if (transition.id != other.transition.id) return false

        return true
    }

    override fun hashCode(): Int {
        return transition.id.hashCode()
    }
}

data class FlowWrapper(val flow: Flow) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FlowWrapper

        if (PlaceWrapper(flow.place) != PlaceWrapper(other.flow.place)) return false
        if (TransitionWrapper(flow.transition) != TransitionWrapper(other.flow.transition))
            return false

        return true
    }

    override fun hashCode(): Int {
        var result = PlaceWrapper(flow.place).hashCode()
        result = 31 * result + TransitionWrapper(flow.transition).hashCode()
        return result
    }

}
