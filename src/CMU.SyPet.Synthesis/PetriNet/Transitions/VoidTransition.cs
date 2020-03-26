using System.Collections.Generic;

namespace CMU.SyPet.Synthesis
{
    public struct VoidTransition<TType> : ITransition
    {
        public IList<TType> ParameterTypes { get; set; }
    }
}
