using System.Linq;
using System.Collections.Generic;
using System;
using CMU.Commons.Exceptions;

namespace CMU.SyPet.Synthesis
{
    public class PetriNetBuilder<TPlace> where TPlace : class, IEquatable<TPlace>
    {
        public PetriNetBuilder() { }

        public virtual DuplicateNodeError? Add(TPlace type) => Insert(type);

        public virtual DuplicateNodeError? Add(IEnumerable<TPlace> types)
        {
            foreach (var type in types)
            {
                var error = Insert(type);

                if (error != null)
                { return error; }
            }

            return null;
        }

        public virtual DuplicateNodeError? AddVoidType(TPlace type)
        {
            var error = Insert(type);

            if (error == null)
            { _voidType = type; }

            return error;
        }

        public virtual IPetriNetBuildError? Add(CastTransition<TPlace> transition)
        {
            IEnumerable<IPetriNetBuildError?> steps()
            {
                yield return Insert(transition);
                yield return InsertArc(from: transition.SubType, to: transition, weight: 1);
                yield return InsertArc(from: transition, to: transition.SuperType, weight: 1);
            }

            return Apply(steps());
        }

        public virtual IPetriNetBuildError? Add(CloneTransition<TPlace> transition)
        {
            IEnumerable<IPetriNetBuildError?> steps()
            {
                yield return Insert(transition);
                yield return InsertArc(from: transition.Type, to: transition, weight: 1);
                yield return InsertArc(from: transition, to: transition.Type, weight: 1);
            }

            return Apply(steps());
        }

        public virtual IPetriNetBuildError? Add(FunctionTransition<TPlace> transition)
        {
            IEnumerable<IPetriNetBuildError?> steps()
            {
                yield return Insert(transition);
                yield return InsertArc(from: transition.ParameterTypes, to: transition);
                yield return InsertArc(from: transition, to: transition.ReturnType, weight: 1);
            };

            return Apply(steps());
        }

        public IPetriNetBuildError? Add(VoidTransition<TPlace> transition)
        {
            if (_voidType == null)
            { return new VoidTypeIsNotDefinedError(); }

            IEnumerable<IPetriNetBuildError?> steps()
            {
                yield return Insert(transition);
                yield return InsertArc(from: transition.ParameterTypes, to: transition);
                yield return InsertArc(from: transition, to: _voidType!, weight: 1);
            }

            return Apply(steps());
        }

        public TPetriNet Build<TPetriNet>() where TPetriNet : IPetriNet<TPlace>, new()
        {
            var net = new TPetriNet();

            foreach (var place in _places)
            { net.Add(place); }

            foreach (var transition in _transitions)
            { net.Add(transition); }

            foreach (var ((place, transition), weight) in _inputArcs)
            { net.AddArc(from: place, to: transition, weight: weight); }

            foreach (var ((transition, place), weight) in _outputArcs)
            { net.AddArc(from: transition, to: place, weight: weight); }

            return net;
        }

        // =========================================================================================
        // Private.
        // =========================================================================================

        private readonly ISet<TPlace> _places = new HashSet<TPlace>();

        private readonly ISet<ITransition> _transitions = new HashSet<ITransition>();

        private readonly IDictionary<(TPlace, ITransition), int> _inputArcs =
            new Dictionary<(TPlace, ITransition), int>();

        private readonly IDictionary<(ITransition, TPlace), int> _outputArcs =
            new Dictionary<(ITransition, TPlace), int>();

        private TPlace? _voidType;

        private DuplicateNodeError? Insert(TPlace place)
        {
            if (_places.Contains(place))
            { return new DuplicateNodeError(); }

            _places.Add(place);
            return null;
        }

        private DuplicateNodeError? Insert(ITransition transition)
        {
            if (_transitions.Contains(transition))
            { return new DuplicateNodeError(); }

            _transitions.Add(transition);
            return null;
        }

        private IPetriNetBuildError? InsertArc(TPlace from, ITransition to, int weight)
        {
            if (!_places.Contains(from) || !_transitions.Contains(to))
            { return new NoSuchNodeError(); }

            if (_inputArcs.ContainsKey((from, to)))
            { return new DuplicateArcError(); }

            _inputArcs.Add((from, to), weight);
            return null;
        }

        private IPetriNetBuildError? InsertArc(ITransition from, TPlace to, int weight)
        {
            if (!_transitions.Contains(from) || !_places.Contains(to))
            { return new NoSuchNodeError(); }

            if (_outputArcs.ContainsKey((from, to)))
            { return new DuplicateArcError(); }

            _outputArcs.Add((from, to), weight);
            return null;
        }

        private IPetriNetBuildError? InsertArc(IEnumerable<TPlace> from, ITransition to)
        {
            // Count how many times each type appears in the parameter types.
            var counts = from.GroupBy(x => x).Select(x => (x.First(), x.Count()));
            IPetriNetBuildError? error = null;

            foreach (var (type, count) in counts)
            {
                error = InsertArc(from: type, to: to, weight: count);

                if (error is DuplicateArcError)
                {
                    // The transition is assumed to have been just added, which means that there
                    // should be no arc connecting these nodes.
                    throw new InternalErrorException(
                        $"Arc from {type} to {to} was already added.");
                }
            }

            return error;
        }

        /// <summary>Apply a sequence of steps upto the first error.</summary>
        private static IPetriNetBuildError? Apply(IEnumerable<IPetriNetBuildError?> steps) =>
            // Return the first non-null element, if one exists, otherwise return null.
            steps.Where(x => x != null).FirstOrDefault();
    }
}
