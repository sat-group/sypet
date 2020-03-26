using System.Collections.Generic;

namespace CMU.SyPet.Originals.Internals
{
    internal class Spec
    {
        public Spec(
            ISet<TypeName> types,
            TypeName voidType,
            ISet<(TypeName subType, TypeName superType)> superTypeRelation,
            ISet<FunctionSignature> functionSignatures,
            FunctionSignature targetFunctionSignature,
            IEnumerable<Program> testCases)
        {
            Types = types;
            VoidType = voidType;
            SuperTypeRelation = superTypeRelation;
            FunctionSignatures = functionSignatures;
            TargetFunctionSignature = targetFunctionSignature;
            TestCases = testCases;
        }

        public ISet<TypeName> Types { get; }

        public TypeName VoidType { get; }

        public ISet<(TypeName subType, TypeName superType)> SuperTypeRelation { get; }

        public ISet<FunctionSignature> FunctionSignatures { get; }

        public FunctionSignature TargetFunctionSignature { get; }

        public IEnumerable<Program> TestCases { get; }
    }
}
