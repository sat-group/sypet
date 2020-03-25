﻿using System.Collections.Generic;

namespace CMU.PetriNets
{
    /// <summary>Type of Petri nets given as input to the reachability analysis algorithm.</summary>
    public interface IPetriNet
    {
        ISet<IPlace> Places { get; }
        
        ISet<ITransition> Transitions { get; }
    }
}
