using Xunit;
using System.Linq;
using static CMU.SyPet.Synthesis.PetriNetTests.TestDataGenerator;
using static CMU.SyPet.Synthesis.PetriNetTests.TestUtils;

namespace CMU.SyPet.Synthesis.PetriNetTests
{
    using Builder = PetriNetBuilder<FakePlace>;

    public class Build
    {
        [Theory]
        [MemberData(
            nameof(PlaceTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Petri_Net_Containing_Added_Place(FakePlace place)
        {
            // Arrange.
            var builder = new PetriNetBuilder<FakePlace>();

            // Act.
            builder.Add(place);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.True(net.Contains(place));
        }

        [Theory]
        [MemberData(
            nameof(CastTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Petri_Net_Containing_Added_Cast_Transition(
            CastTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: transition, to: builder);

            // Act.
            builder.Add(transition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.True(net.Contains(transition));
            Assert.Equal(
                actual: net.GetWeight(from: transition.SubType, to: transition),
                expected: 1);
            Assert.Equal(
                actual: net.GetWeight(from: transition, to: transition.SuperType),
                expected: 1);
        }

        [Theory]
        [MemberData(
            nameof(CloneTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Petri_Net_Containing_Added_Clone_Transition(
            CloneTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: transition, to: builder);

            // Act.
            builder.Add(transition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.True(net.Contains(transition));
        }

        [Theory]
        [MemberData(
            nameof(FunctionTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Petri_Net_Containing_Added_Function_Transition(
            FunctionTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: transition, to: builder);

            // Act.
            builder.Add(transition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.True(net.Contains(transition));
        }

        [Theory]
        [MemberData(
            nameof(VoidTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Petri_Net_Containing_Added_Void_Transition(
            VoidTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: transition, to: builder);
            builder.AddVoidType(VoidTypeGenerator());

            // Act.
            builder.Add(transition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.True(net.Contains(transition));
        }

        [Theory]
        [MemberData(
            nameof(CastTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Petri_Net_With_Arcs_To_And_From_A_Given_Added_Cast_Transition(
            CastTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: transition, to: builder);

            // Act.
            builder.Add(transition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.True(net.ContainsArc(from: transition.SubType, to: transition));
            Assert.True(net.ContainsArc(from: transition, to: transition.SuperType));
        }

        [Theory]
        [MemberData(
            nameof(CloneTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Petri_Net_With_Arcs_To_And_From_A_Given_Added_Clone_Transition(
            CloneTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: transition, to: builder);

            // Act.
            builder.Add(transition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.True(net.ContainsArc(from: transition.Type, to: transition));
            Assert.True(net.ContainsArc(from: transition, to: transition.Type));
            Assert.Equal(
                actual: net.GetWeight(from: transition.Type, to: transition),
                expected: 1);
            Assert.Equal(
                actual: net.GetWeight(from: transition, to: transition.Type),
                expected: 1);
        }

        [Theory]
        [MemberData(
            nameof(FunctionTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Petri_Net_With_Arcs_To_And_From_A_Given_Added_Function_Transition(
            FunctionTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: transition, to: builder);

            // Act.
            builder.Add(transition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.All(
                transition.ParameterTypes,
                type => Assert.True(net.ContainsArc(from: type, to: transition)));
            Assert.True(net.ContainsArc(from: transition, to: transition.ReturnType));
            Assert.All(
                transition.ParameterTypes,
                type => Assert.Equal(
                    actual: net.GetWeight(from: type, to: transition),
                    expected: transition.ParameterTypes.Where(t => t.Equals(type)).Count()));
            Assert.Equal(
                actual: net.GetWeight(from: transition, to: transition.ReturnType),
                expected: 1);
        }

        [Theory]
        [MemberData(
            nameof(VoidTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Petri_Net_With_Arcs_To_And_From_A_Given_Added_Void_Transition(
            VoidTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: transition, to: builder);
            builder.AddVoidType(VoidTypeGenerator());

            // Act.
            builder.Add(transition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.All(
                transition.ParameterTypes,
                type => Assert.True(net.ContainsArc(from: type, to: transition)));
            Assert.True(net.ContainsArc(from: transition, to: VoidTypeGenerator()));
            Assert.All(
                transition.ParameterTypes,
                type => Assert.Equal(
                    actual: net.GetWeight(from: type, to: transition),
                    expected: transition.ParameterTypes.Where(t => t.Equals(type)).Count()));
            Assert.Equal(
                actual: net.GetWeight(from: transition, to: VoidTypeGenerator()),
                expected: 1);
        }
    }
}
