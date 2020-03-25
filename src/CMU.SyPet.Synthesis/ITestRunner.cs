using System.Collections.Generic;

namespace CMU.SyPet.Synthesis
{
    public interface ITestRunner<TProgram, TTestResult>
    {
        TTestResult Run(TProgram program, IEnumerable<TProgram> testCases);
    }
}
