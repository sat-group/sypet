using System.Collections.Generic;

namespace CMU.SyPet.Synthesis
{
    internal sealed class FakePetriNet : IPetriNet<FakePlace>
    {
        public ISet<FakePlace> Places { get; } = new HashSet<FakePlace>();

        public ISet<ITransition> Transitions { get; } = new HashSet<ITransition>();

        public IDictionary<(FakePlace, ITransition), int> InputArcs { get; } =
            new Dictionary<(FakePlace, ITransition), int>();

        public IDictionary<(ITransition, FakePlace), int> OutputArcs { get; } =
            new Dictionary<(ITransition, FakePlace), int>();

        public void Add(FakePlace place) => Places.Add(place);

        public void Add(ITransition transition) => Transitions.Add(transition);

        public void AddArc(FakePlace from, ITransition to, int weight) =>
            InputArcs.Add((from, to), weight);

        public void AddArc(ITransition from, FakePlace to, int weight) =>
            OutputArcs.Add((from, to), weight);

        internal bool Contains(FakePlace place) => Places.Contains(place);

        internal bool Contains(ITransition transition) => Transitions.Contains(transition);

        internal bool ContainsArc(FakePlace from, ITransition to) => 
            InputArcs.ContainsKey((from, to));

        internal bool ContainsArc(ITransition from, FakePlace to) => 
            OutputArcs.ContainsKey((from, to));

        internal int GetWeight(FakePlace from, ITransition to) => InputArcs[(from, to)];

        internal int GetWeight(ITransition from, FakePlace to) => OutputArcs[(from, to)];
    }
}
