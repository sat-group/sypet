using System.Collections.Generic;

namespace CMU.SyPet.Synthesis
{
    public abstract class TestRunnerOracle<TQuery, TResponse, TProgram, TTestResult> 
        : IOracle<TQuery, TResponse>
    {
        protected ITestRunner<TProgram, TTestResult> TestRunner { get; }

        protected TestRunnerOracle(ITestRunner<TProgram, TTestResult> testRunner)
        {
            TestRunner = testRunner;
        }

        protected abstract IEnumerable<TProgram> CreateTestCases(TQuery query);

        protected abstract TProgram GetProgram(TQuery query);

        protected abstract TResponse CreateResponse(TTestResult testResult);

        public virtual TResponse Respond(TQuery query)
        {
            var program = GetProgram(query);
            var testCases = CreateTestCases(query);
            var testResult = TestRunner.Run(program, testCases);
            var response = CreateResponse(testResult);

            return response;
        }
    }
}
