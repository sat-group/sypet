namespace CMU.SyPet.Synthesis
{
    public struct CloneTransition<TType> : ITransition
    {
        public TType Type { get; set; }
    }
}
