using System.Collections.Generic;

namespace CMU.SyPet.Synthesis
{
    public struct VoidTransition<TType> : ITransition
    {
        public List<TType> ParameterTypes { get; set; }
    }
}
