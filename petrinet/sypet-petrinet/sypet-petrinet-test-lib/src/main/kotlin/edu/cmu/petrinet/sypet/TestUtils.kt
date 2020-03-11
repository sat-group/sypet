package edu.cmu.petrinet.sypet

internal fun  PetriNetBuilder.addMethodTypes(
    signature: MethodSignature
) {
    addPlace(signature.returnType())

    for (type in signature.parametersTypes().toSet()) {
        addPlace(type)
    }
}