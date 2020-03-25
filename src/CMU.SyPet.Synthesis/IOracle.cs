namespace CMU.SyPet.Synthesis
{
    public interface IOracle<TQuery, TResponse>
    {
        TResponse Respond(TQuery query);
    }
}
