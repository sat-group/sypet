using System;
using System.Collections.Generic;
using System.Linq;
using CMU.Commons.Types;
using CMU.PetriNets;
using CMU.SyPet.Synthesis;

namespace CMU.SyPet.Originals.Internals
{
    internal class Sketcher : ISketcher<Spec, Sketch, Unit>
    {
        public Sketcher(IReachabilityAnalyzer<ITransition> reachabilityAnalyzer)
        {
            ReachabilityAnalyzer = reachabilityAnalyzer;
        }

        public IEnumerable<Sketch> Sketch(Spec spec)
        {
            var net = BuildPetriNet(spec);

            foreach (var path in ReachabilityAnalyzer.FindAllPaths(net))
            { yield return BuildSketch(path); }
        }

        public IEnumerable<Sketch> Sketch(Spec spec, Unit hint) => Sketch(spec);

        internal static SyPetriNet BuildPetriNet(Spec spec)
        {
            var builder = new PetriNetBuilder<TypeName>();

            builder.AddVoidType(spec.VoidType);
            builder.Add(spec.Types);

            foreach (var type in spec.Types)
            { builder.Add(new CloneTransition<TypeName> { Type = type }); }

            foreach (var element in spec.SuperTypeRelation)
            {
                builder.Add(new CastTransition<TypeName>
                {
                    SubType = element.subType,
                    SuperType = element.superType
                });
            }

            foreach (var function in spec.FunctionSignatures)
            {
                builder.Add(new FunctionTransition<TypeName>
                {
                    ParameterTypes = function.ParameterTypes,
                    ReturnType = function.ReturnType
                });

                builder.Add(new VoidTransition<TypeName>
                { ParameterTypes = function.ParameterTypes });
            }

            return builder.Build<SyPetriNet>();
        }

        internal static Sketch BuildSketch(IList<ITransition> path)
        {
            throw new NotImplementedException();
        }

        // =========================================================================================
        // Private.
        // =========================================================================================

        private IReachabilityAnalyzer<ITransition> ReachabilityAnalyzer { get; }
    }
}
