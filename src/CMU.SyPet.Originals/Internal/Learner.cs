using System.Linq;
using System.Collections.Generic;
using CMU.Commons.Types;
using CMU.SyPet.Synthesis;

namespace CMU.SyPet.Originals.Internals
{
    /// <summary>The original SyPet learner based on Petri net reachability.</summary>
    internal sealed class Learner :
        SketchBasedLearner<Spec, Program, Program, Response, Sketch, Unit, Unit>
    {
        public Learner(
            ISketcher<Spec, Sketch, Unit> sketcher,
            ISketchSolver<Sketch, Program, Unit> sketchSolver,
            Spec spec
        ) : base(sketcher, sketchSolver, spec) { }

        protected override Program CreateQuery(Program program) => program;

        protected override Program? CreateResult(Response response) => response.Program;

        // Our sketcher is dumb and does not know how to take a hint.
        protected override Unit CreateSketcherHint(Response response) => new Unit();

        // Our sketch solver is dumb and does not know how to take a hint.
        protected override Unit CreateSketchSolverHint(Response response) => new Unit();

        protected override bool Finished(Response response) => response.IsCorrectProgram;

        protected override IEnumerable<Sketch> SelectSketches(IEnumerable<Sketch> sketches) =>
            sketches;

        protected override Program SelectProgram(IEnumerable<Program> programs) =>
            programs.First();
    }
}
