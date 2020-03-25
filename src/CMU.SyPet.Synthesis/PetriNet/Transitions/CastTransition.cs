namespace CMU.SyPet.Synthesis
{
    public struct CastTransition<TType> : ITransition
    {
        public TType SubType { get; set; }
        public TType SuperType { get; set; }
    }
}
