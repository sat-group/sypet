using CMU.Commons.Types;

namespace CMU.SyPet.Synthesis
{
    internal class Synthesizer<TSpec, TProgram, TQuery, TResponse> : ISynthesizer<TSpec, TProgram>
        where TProgram : class
        where TQuery : class
    {
        private ILearner<TSpec, TProgram, TQuery, TResponse> Learner { get; }
        private IOracle<TQuery, TResponse> Oracle { get; }

        internal Synthesizer(
            ILearner<TSpec, TProgram, TQuery, TResponse> learner,
            IOracle<TQuery, TResponse> oracle)
        {
            Learner = learner;
            Oracle = oracle;
        }

        public TProgram? TrySynthesize(TSpec spec)
        {
            var query = Learner.FirstQuery(spec);

            if (query == null)
            {
                return null;
            }

            TProgram? result = null;

            while (true)
            {
                var response = Oracle.Respond(query);
                var outcome = Learner.Learn(spec, response);

                outcome.IfLeft(newQuery => query = newQuery);
                outcome.IfRight(program => result = program);

                if (result != null)
                {
                    return result;
                }
            }
        }
    }
}
