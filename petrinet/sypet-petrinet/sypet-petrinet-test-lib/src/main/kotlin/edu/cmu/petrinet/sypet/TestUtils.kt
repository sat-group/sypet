package edu.cmu.petrinet.sypet

internal fun  PetriNetBuilder.addMethodTypes(
    signature: MethodSignature
) {
    add(signature.returnType())

    for (type in signature.parametersTypes().toSet()) {
        add(type)
    }
}