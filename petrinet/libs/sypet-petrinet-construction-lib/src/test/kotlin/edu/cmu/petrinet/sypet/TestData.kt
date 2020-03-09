package edu.cmu.petrinet.sypet

val signatures = listOf<MethodSignature>(
    DefaultMethodSignature(
        parametersTypes = mutableListOf(),
        returnType = DefaultType(
            name = "retType"
        )
    ),
    DefaultMethodSignature(
        parametersTypes = mutableListOf(
            DefaultType(name = "paramType1"),
            DefaultType(name = "paramType2"),
            DefaultType(name = "paramType2")
        ),
        returnType = DefaultType(name = "retType")
    )
)

val types = listOf<Type>(
    DefaultType(isCastableTo = false),
    DefaultType(isCastableTo = true)
)