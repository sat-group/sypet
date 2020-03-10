package edu.cmu.petrinet.sypet

internal fun <T, U> PetriNetBuilder<T, U>.addMethodTypes(
    signature: MethodSignature<T, U>
) {
    addPlace(signature.returnType())

    for (type in signature.parametersTypes().toSet()) {
        addPlace(type)
    }
}