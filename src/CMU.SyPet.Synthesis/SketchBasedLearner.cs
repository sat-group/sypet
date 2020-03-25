using System;
using System.Collections.Generic;
using System.Linq;
using CMU.Commons.Types;

namespace CMU.SyPet.Synthesis
{
    /// <summary>Class of learners that work by constructing program sketches out of the
    /// specification and subsequently solving them in order to produce candidate programs.
    /// </summary>
    public abstract class SketchBasedLearner
        <TSpec, TProgram, TQuery, TResponse, TSketch, TSketchHint, TSketchSolverHint>
        : ILearner<TSpec, TProgram, TQuery, TResponse>
        where TProgram : class
        where TQuery : class
    {
        protected ISketcher<TSpec, TSketch, TSketchHint> Sketcher { get; }

        protected ISketchSolver<TSketch, TProgram, TSketchSolverHint> SketchSolver { get; }

        public SketchBasedLearner(
            ISketcher<TSpec, TSketch, TSketchHint> sketcher,
            ISketchSolver<TSketch, TProgram, TSketchSolverHint> sketchSolver,
            TSpec spec)
        {
            Sketcher = sketcher;
            SketchSolver = sketchSolver;
        }

        /// <summary>Create a hint to give to the sketcher out of the response given by
        /// oracle.</summary>
        protected abstract TSketchHint CreateSketcherHint(TResponse response);

        /// <summary>Create a hint to give to the sketch solver out of the response given by the
        /// oracle.</summary>
        protected abstract TSketchSolverHint CreateSketchSolverHint(TResponse response);
        // ^ We should consider changing this to depend also on the sketch that solver will try to
        // solve.

        /// <summary>A hook to filter/prioritize the sketches. It could be automatic (for example,
        /// the application of a machine learning model) or manual (for example, the end user picks
        /// the sketches interactively).</summary>
        protected abstract IEnumerable<TSketch> PickSketches(IEnumerable<TSketch> sketches);

        /// <summary>A hook to process the sketches before passing them to the sketch solver. It
        /// could be automatic (for example, the application of a machine learning model) or manual
        /// (for example, the end user picks the sketches interactively).</summary>
        protected abstract IEnumerable<TSketch> ProcessSketches(IEnumerable<TSketch> sketch);

        /// <summary>Create a query to the oracle out of a candidate program.</summary>
        protected abstract TQuery CreateQuery(TProgram sketch);

        /// <summary>Create a learning result out of an oracle response.</summary>
        protected abstract Either<TQuery, TProgram?> CreateResult(TResponse response);

        public virtual TQuery? FirstQuery(TSpec spec)
        {
            var sketches = Sketcher.Sketch(spec);
            var programs = FindCandidatePrograms(sketches, SketchSolver.Solve);

            return (programs.Any()) ? CreateQuery(programs.First()) : null;
        }

        public virtual Either<TQuery, TProgram?> Learn(TSpec spec, TResponse response)
        {
            var sketcherHint = CreateSketcherHint(response);
            var sketchSolverHint = CreateSketchSolverHint(response);
            var sketches = Sketcher.Sketch(spec, sketcherHint);
            var programs = FindCandidatePrograms(
                sketches, 
                sketch => SketchSolver.Solve(sketch, sketchSolverHint));
            var result = CreateResult(response);

            return result;
        }

        private IEnumerable<TProgram> FindCandidatePrograms(
            IEnumerable<TSketch> sketches,
            Func<TSketch, IEnumerable<TProgram>> solver)
        {
            var pickedSketches = PickSketches(sketches);
            var processedSketches = ProcessSketches(pickedSketches);
            var programs = processedSketches.SelectMany(solver);

            if (!programs.Any())
            {
                // Go back and pick or process the sketches again?
            }

            return programs;
        }
    }
}
