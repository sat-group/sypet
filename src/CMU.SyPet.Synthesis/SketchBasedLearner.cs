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

        /// <summary>A hook to apply some kind of operation to the sketches before passing them to
        /// the sketch solver. It could be automatic (for example, the application of a machine
        /// learning model) or manual (for example, the end user picks the sketches
        /// interactively).</summary>
        protected abstract IEnumerable<TSketch> SelectSketches(IEnumerable<TSketch> sketches);

        /// <summary>A hook to pick and apply some kind of operation to a program before passing
        /// creating a query. It could be automatic (for example, the application of a machine
        /// learning model) or manual (for example, the end user picks the sketches
        /// interactively).</summary>
        /// <remarks>This method assumes that the input is non-empty.</remarks>
        protected abstract TProgram SelectProgram(IEnumerable<TProgram> programs);

        /// <summary>Create a query to the oracle out of a candidate program.</summary>
        protected abstract TQuery CreateQuery(TProgram program);

        /// <summary>Create a learning result out of an oracle response.</summary>
        /// <remark>This method is called when the learning process is finished.</remark>
        protected abstract TProgram? CreateResult(TResponse response);

        /// <summary>Decide whether the learning process is finished and we can produce a
        /// result.</summary>
        protected abstract bool Finished(TResponse response);

        public virtual TQuery? FirstQuery(TSpec spec)
        {
            var sketches = Sketcher.Sketch(spec);
            var programs = FindCandidatePrograms(sketches, SketchSolver.Solve);

            return (programs.Any()) ? CreateQuery(programs.First()) : null;
        }

        public virtual Either<TQuery, TProgram?> Learn(TSpec spec, TResponse response)
        {
            if (Finished(response))
            { return CreateResult(response); }

            var sketcherHint = CreateSketcherHint(response);
            var sketchSolverHint = CreateSketchSolverHint(response);
            var sketches = Sketcher.Sketch(spec, sketcherHint);
            var programs = FindCandidatePrograms(
                sketches, 
                sketch => SketchSolver.Solve(sketch, sketchSolverHint));

            if (!programs.Any())
            {
                // Go back and select the programs again?
            }

            var candidateProgram = SelectProgram(programs);

            return CreateQuery(candidateProgram);
        }

        protected virtual IEnumerable<TProgram> FindCandidatePrograms(
            IEnumerable<TSketch> sketches,
            Func<TSketch, IEnumerable<TProgram>> solver)
        {
            var processedSketches = SelectSketches(sketches);
            var programs = processedSketches.SelectMany(solver);

            if (!programs.Any())
            {
                // Go back and pick or process the sketches again?
            }

            return programs;
        }
    }
}
