namespace CMU.SyPet.Synthesis
{
    public interface ISynthesizer<Spec, Program> where Program : class
    {
        Program? TrySynthesize(Spec spec);
    }
}
