namespace CMU.SyPet.Synthesis.PetriNetTests
{
    using Builder = PetriNetBuilder<FakePlace>;

    public static class TestUtils
    {
        public static void AddPlaces(CastTransition<FakePlace> from, Builder to)
        {
            to.Add(from.SubType);
            to.Add(from.SuperType);
        }

        public static void AddPlaces(CloneTransition<FakePlace> from, Builder to) => 
            to.Add(from.Type);

        public static void AddPlaces(FunctionTransition<FakePlace> from, Builder to)
        {
            to.Add(from.ParameterTypes);
            to.Add(from.ReturnType);
        }

        public static void AddPlaces(VoidTransition<FakePlace> from, Builder to) =>
            to.Add(from.ParameterTypes);
    }
}
