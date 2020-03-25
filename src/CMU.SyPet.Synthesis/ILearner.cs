using CMU.Commons.Types;

namespace CMU.SyPet.Synthesis
{
    public interface ILearner<TSpec, TProgram, TQuery, TResponse> 
        where TProgram : class
        where TQuery : class
    {
        TQuery? FirstQuery(TSpec spec);

        Either<TQuery, TProgram?> Learn(TSpec spec, TResponse response);
    }
}
