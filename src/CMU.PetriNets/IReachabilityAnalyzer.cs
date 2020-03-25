using System;
using System.Collections.Generic;

namespace CMU.PetriNets
{
    public interface IReachabilityAnalyzer
    {
        IEnumerable<ITransition> FindPaths(IPetriNet petriNet);
    }
}
