package edu.cmu.petrinet.apt

import edu.cmu.petrinet.sypet.*
import uniol.apt.adt.pn.*

private val Type.id
    get() = hashCode().toString()

private val MethodSignature.id
    get() = hashCode().toString()

class SyPetBackendAdapter : BackendPetriNet<Type, MethodSignature> {
    private val net: PetriNet = PetriNet()

    override fun addPlace(type: Type?) {
        requireNotNull(type)
        requireNotContainsPlace(type)

        this.net.createPlace(type.id)
    }

    override fun addTransition(signature: MethodSignature?) {
        requireNotNull(signature)
        requireNotContainsTransition(signature)

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
        requireContainsPlace(type)
        requireContainsTransition(signature)
        requirePlaceNotAdjacentToTransition(type, signature)

        this.net.createFlow(type.id, signature.id)
    }

    override fun addArcFromTransitionToPlace(
        signature: MethodSignature?,
        type: Type?,
        weight: Int?
    ) {
        requireNotNull(signature)
        requireNotNull(type)
        requireNotNull(weight)
        requireContainsTransition(signature)
        requireContainsPlace(type)
        requireTransitionNotAdjacentToPlace(signature, type)

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
        requireContainsPlace(type)
        requireContainsTransition(signature)

        return this.net.getPlace(type.id).postset.any { transition ->
            transition.id == signature.id
        }
    }

    override fun isTransitionAdjacentToPlace(signature: MethodSignature?, type: Type?): Boolean {
        requireNotNull(signature)
        requireNotNull(type)
        requireContainsTransition(signature)
        requireContainsPlace(type)

        return this.net.getTransition(signature.id).postset.any { place ->
            place.id == type.id
        }
    }

    override fun getArcWeightFromTypeToSignature(type: Type?, signature: MethodSignature?): Int {
        requirePlaceAdjacentToTransition(type, signature)
        return this.net.getFlow(type!!.id, signature!!.id).weight
    }

    override fun getArcWeightFromSignatureToType(signature: MethodSignature?, type: Type?): Int {
        requireTransitionAdjacentToPlace(signature, type)
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

    private fun requireContainsPlace(type: Type) {
        if (!this.net.containsPlace(type.id)) {
            throw NoSuchPlaceException(type)
        }
    }

    private fun requireNotContainsPlace(type: Type) {
        if (this.net.containsPlace(type.id)) {
            throw PlaceAlreadyExistsException(type)
        }
    }

    private fun requireContainsTransition(signature: MethodSignature) {
        if (!this.net.containsTransition(signature.id)) {
            throw NoSuchTransitionException(signature)
        }
    }

    private fun requireNotContainsTransition(signature: MethodSignature) {
        if (this.net.containsTransition(signature.id)) {
            throw TransitionAlreadyExistsException(signature)
        }
    }

    private fun requirePlaceNotAdjacentToTransition(type: Type, signature: MethodSignature) {
        if (this.isPlaceAdjacentToTransition(type, signature)) {
            throw ArcAlreadyExistsException(
                type,
                signature
            )
        }
    }

    private fun requireTransitionNotAdjacentToPlace(signature: MethodSignature, type: Type) {
        if (this.isTransitionAdjacentToPlace(signature, type)) {
            throw ArcAlreadyExistsException(signature, type)
        }
    }

    private fun requirePlaceAdjacentToTransition(type: Type?, signature: MethodSignature?) {
        if (!this.isPlaceAdjacentToTransition(type, signature)) {
            throw NoSuchArcException(type, signature)
        }
    }

    private fun requireTransitionAdjacentToPlace(signature: MethodSignature?, type: Type?) {
        if (!this.isTransitionAdjacentToPlace(signature, type)) {
            throw NoSuchArcException(signature, type)
        }
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
