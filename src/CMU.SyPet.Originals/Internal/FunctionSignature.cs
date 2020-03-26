using System.Collections.Generic;

namespace CMU.SyPet.Originals.Internals
{
    internal class FunctionSignature
    {
        public FunctionSignature(string name, IList<TypeName> parameterTypes, TypeName returnType)
        {
            Name = name;
            ParameterTypes = parameterTypes;
            ReturnType = returnType;
        }

        public string Name { get; }

        public IList<TypeName> ParameterTypes { get; }

        public TypeName ReturnType { get; }
    }
}
