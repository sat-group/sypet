using System.Collections.Generic;
using CMU.PetriNets;
using CMU.SyPet.Synthesis;

namespace CMU.SyPet.Originals.Internals
{
    internal class SyPetriNet : 
        CMU.PetriNets.IPetriNet<ITransition>, 
        CMU.SyPet.Synthesis.IPetriNet<TypeName>
    {
        public ISet<IPlace> Places => throw new System.NotImplementedException();

        public ISet<ITransition> Transitions => throw new System.NotImplementedException();

        public void Add(TypeName place)
        {
            throw new System.NotImplementedException();
        }

        public void Add(Synthesis.ITransition transition)
        {
            throw new System.NotImplementedException();
        }

        public void AddArc(TypeName from, Synthesis.ITransition to, int weight)
        {
            throw new System.NotImplementedException();
        }

        public void AddArc(Synthesis.ITransition from, TypeName to, int weight)
        {
            throw new System.NotImplementedException();
        }
    }
}
