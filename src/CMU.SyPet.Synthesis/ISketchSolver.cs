using System.Collections.Generic;
namespace CMU.SyPet.Synthesis
{
    public interface ISketchSolver<TSketch, TProgram, THint>
    {
        IEnumerable<TProgram> Solve(TSketch sketch);

        IEnumerable<TProgram> Solve(TSketch sketch, THint hint);
    }
}
