using System;
using System.Collections.Generic;

namespace CMU.PetriNets
{
    public interface IReachabilityAnalyzer<TTransition>
    {
        // A path is a list of transitions that are fired in order to go from an initial marking to
        // a goal marking.
        // Fixme: This signature is not taking into account the markings.
        IEnumerable<IList<TTransition>> FindAllPaths(IPetriNet<TTransition> petriNet);
    }
}
