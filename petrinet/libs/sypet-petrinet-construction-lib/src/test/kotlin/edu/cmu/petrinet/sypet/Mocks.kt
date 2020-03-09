package edu.cmu.petrinet.sypet

data class DefaultType(
    private val name: String = "Any"
) : Type {
    override fun name(): String {
        return name
    }
}

data class DefaultMethodSignature(
    private val returnType: Type = DefaultType(),
    private val name: String = "Method",
    private val parametersTypes: MutableList<Type> = MutableList(size = 3) { DefaultType() }
) : MethodSignature {
    override fun returnType(): Type {
        return returnType
    }

    override fun name(): String {
        return name
    }

    override fun parametersTypes(): MutableList<Type> {
        return parametersTypes
    }
}
