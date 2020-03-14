package edu.cmu.petrinet.sypet

internal fun  PetriNetBuilder.addMethodTypes(
    transition: MethodTransition
) {
    add(transition.returnType())

    for (type in transition.parametersTypes().toSet()) {
        add(type)
    }
}