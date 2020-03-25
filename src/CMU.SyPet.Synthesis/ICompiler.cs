namespace CMU.SyPet.Synthesis
{
    public interface ICompiler<TProgram, TCompiledProgram>
    {
        TCompiledProgram Compile(TProgram program);
    }
}
