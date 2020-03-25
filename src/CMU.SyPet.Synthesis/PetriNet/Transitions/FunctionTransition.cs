using System.Collections.Generic;

namespace CMU.SyPet.Synthesis
{
    public struct FunctionTransition<TType> : ITransition
    {
        public List<TType> ParameterTypes { get; set; }
        public TType ReturnType { get; set; }
    }
}
