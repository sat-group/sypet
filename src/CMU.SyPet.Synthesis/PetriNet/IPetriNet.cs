using System.Collections.Generic;
using CMU.Commons.Types;

namespace CMU.SyPet.Synthesis
{
    /// <summary>The Petri net builder works with any Petri net that implements this interface.
    /// </summary>
    public interface IPetriNet<TPlace>
    {
        void Add(TPlace place);

        void Add(ITransition transition);

        void AddArc(TPlace from, ITransition to, int weight);

        void AddArc(ITransition from, TPlace to, int weight);
    }
}
