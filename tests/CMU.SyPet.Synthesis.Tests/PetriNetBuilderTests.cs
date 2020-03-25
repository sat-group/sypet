using Xunit;
using System.Linq;

namespace CMU.SyPet.Synthesis
{
    using Builder = PetriNetBuilder<FakePlace>;

    public class PetriNetBuilderTests
    {
        // Stub void type for testing purposes. It is assumed that the test data does not include a
        // place with the same id.
        private readonly FakePlace _stubVoidType = new FakePlace(id: -1);

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.PlaceTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_New_Place_Returns_No_Error(FakePlace place)
        {
            // Arrange.
            var builder = new Builder();

            // Act.
            var error = builder.Add(place);

            // Assert.
            Assert.Null(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.CastTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_New_Cast_Transition_Returns_No_Error(
            CastTransition<FakePlace> castTransition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: castTransition, to: builder);

            // Act.
            var error = builder.Add(castTransition);

            // Assert.
            Assert.Null(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.CloneTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_New_Clone_Transition_Returns_No_Error(
            CloneTransition<FakePlace> cloneTransition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: cloneTransition, to: builder);

            // Act.
            var error = builder.Add(cloneTransition);

            // Assert.
            Assert.Null(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.FunctionTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_New_Function_Transition_Returns_No_Error(
            FunctionTransition<FakePlace> functionTransition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: functionTransition, to: builder);

            // Act.
            var error = builder.Add(functionTransition);

            // Assert.
            Assert.Null(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.VoidTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_New_Void_Transition_Returns_No_Error(
            VoidTransition<FakePlace> voidTransition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: voidTransition, to: builder);
            builder.AddVoidType(_stubVoidType);

            // Act.
            var error = builder.Add(voidTransition);

            // Assert.
            Assert.Null(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.PlaceTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Duplicate_Place_Returns_Duplicate_Node_Error(FakePlace place)
        {
            // Arrange.
            var builder = new PetriNetBuilder<FakePlace>();

            // Act.
            builder.Add(place);
            var error = builder.Add(place);

            // Assert.
            Assert.IsType<DuplicateNodeError>(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.CastTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Duplicate_Cast_Transition_Returns_Duplicate_Node_Error(
            CastTransition<FakePlace> castTransition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: castTransition, to: builder);

            // Act.
            builder.Add(castTransition);
            var error = builder.Add(castTransition);

            // Assert.
            Assert.IsType<DuplicateNodeError>(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.CloneTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Duplicate_Clone_Transition_Returns_Duplicate_Node_Error(
            CloneTransition<FakePlace> cloneTransition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: cloneTransition, to: builder);

            // Act.
            builder.Add(cloneTransition);
            var error = builder.Add(cloneTransition);

            // Assert.
            Assert.IsType<DuplicateNodeError>(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.FunctionTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Duplicate_Function_Transition_Returns_Duplicate_Node_Error(
            FunctionTransition<FakePlace> functionTransition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: functionTransition, to: builder);

            // Act.
            builder.Add(functionTransition);
            var error = builder.Add(functionTransition);

            // Assert.
            Assert.IsType<DuplicateNodeError>(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.VoidTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Duplicate_Void_Transition_Returns_Duplicate_Node_Error(
            VoidTransition<FakePlace> voidTransition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: voidTransition, to: builder);
            builder.AddVoidType(_stubVoidType);

            // Act.
            builder.Add(voidTransition);
            var error = builder.Add(voidTransition);

            // Assert.
            Assert.IsType<DuplicateNodeError>(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.PlaceTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Build_After_Add_Place_Returns_Petri_Net_Containing_The_Place(FakePlace place)
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
            nameof(TestDataGenerator.CastTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Build_After_Add_Cast_Transition_Returns_Petri_Net_Containing_The_Transition(
            CastTransition<FakePlace> castTransition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: castTransition, to: builder);

            // Act.
            builder.Add(castTransition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.True(net.Contains(castTransition));
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.CloneTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Build_After_Add_Clone_Transition_Returns_Petri_Net_Containing_The_Transition(
            CloneTransition<FakePlace> cloneTransition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: cloneTransition, to: builder);

            // Act.
            builder.Add(cloneTransition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.True(net.Contains(cloneTransition));
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.FunctionTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Build_After_Add_Function_Transition_Returns_Petri_Net_Containing_The_Transition(
            FunctionTransition<FakePlace> functionTransition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: functionTransition, to: builder);

            // Act.
            builder.Add(functionTransition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.True(net.Contains(functionTransition));
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.VoidTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Build_After_Add_Void_Transition_Returns_Petri_Net_Containing_The_Transition(
            VoidTransition<FakePlace> voidTransition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: voidTransition, to: builder);
            builder.AddVoidType(_stubVoidType);

            // Act.
            builder.Add(voidTransition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.True(net.Contains(voidTransition));
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.CastTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Cast_Transition_With_Unknown_SubType_Returns_No_Such_Node_Error(
            CastTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();

            // Only add the super type if is different from the sub type. Otherwise, we would
            // violate the assumption that the sub type is unknown to the builder.
            if (!transition.SubType.Equals(transition.SuperType))
            { builder.Add(transition.SuperType); }

            // Act.
            var error = builder.Add(transition);

            // Assert.
            Assert.IsType<NoSuchNodeError>(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.CastTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Cast_Transition_With_Unknown_SuperType_Returns_No_Such_Node_Error(
            CastTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();

            // Only add the sub type if is different from the super type. Otherwise, we would
            // violate the assumption that the super type is unknown to the builder.
            if (!transition.SubType.Equals(transition.SuperType))
            { builder.Add(transition.SubType); }

            // Act.
            var error = builder.Add(transition);

            // Assert.
            Assert.IsType<NoSuchNodeError>(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.CloneTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Clone_Transition_With_Unknown_Type_Returns_No_Such_Node_Error(
            CloneTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();

            // Act.
            var error = builder.Add(transition);

            // Assert.
            Assert.IsType<NoSuchNodeError>(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.FunctionTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Function_Transition_With_Unknown_Parameter_Type_Returns_No_Such_Node_Error(
            FunctionTransition<FakePlace> transition)
        {
            // There are no unknown types if there are no parameter types to begin with.
            if (!transition.ParameterTypes.Any())
            { return; }

            // Arrange.
            var builder = new Builder();

            // Make sure we do not violate the assumption that there is at least one parameter type
            // that is unknown to the builder.
            if (transition.ParameterTypes.Any(type => !type.Equals(transition.ReturnType)))
            { builder.Add(transition.ReturnType); }

            // Act.
            var error = builder.Add(transition);

            // Assert.
            Assert.IsType<NoSuchNodeError>(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.FunctionTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Function_Transition_With_Unknown_Return_Type_Returns_No_Such_Node_Error(
            FunctionTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();

            foreach (var type in transition.ParameterTypes)
            { if (!type.Equals(transition.ReturnType)) { builder.Add(type); } }

            // Act.
            var error = builder.Add(transition);

            // Assert.
            Assert.IsType<NoSuchNodeError>(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.VoidTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Void_Transition_With_Unknown_Parameter_Type_Returns_No_Such_Node_Error(
            VoidTransition<FakePlace> transition)
        {
            // There are no unknown types if there are no parameter types to begin with.
            if (!transition.ParameterTypes.Any())
            { return; }

            // Arrange.
            var builder = new Builder();
            builder.AddVoidType(_stubVoidType);

            // Act.
            var error = builder.Add(transition);

            // Assert.
            Assert.IsType<NoSuchNodeError>(error);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.CastTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Cast_Transition_Creates_Arcs(CastTransition<FakePlace> transition)
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
            nameof(TestDataGenerator.CloneTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Clone_Transition_Creates_Arcs(CloneTransition<FakePlace> transition)
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
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.FunctionTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Function_Transition_Creates_Arcs(FunctionTransition<FakePlace> transition)
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
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.VoidTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Void_Transition_Creates_Arcs(VoidTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: transition, to: builder);
            builder.AddVoidType(_stubVoidType);

            // Act.
            builder.Add(transition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.All(
                transition.ParameterTypes,
                type => Assert.True(net.ContainsArc(from: type, to: transition)));
            Assert.True(net.ContainsArc(from: transition, to: _stubVoidType));
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.CastTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Cast_Transition_Creates_Arcs_With_Correct_Weights(
            CastTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: transition, to: builder);

            // Act.
            builder.Add(transition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.Equal(
                actual: net.GetWeight(from: transition.SubType, to: transition),
                expected: 1);
            Assert.Equal(
                actual: net.GetWeight(from: transition, to: transition.SuperType),
                expected: 1);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.CloneTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Clone_Transition_Creates_Arcs_With_Correct_Weights(
            CloneTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: transition, to: builder);

            // Act.
            builder.Add(transition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.Equal(
                actual: net.GetWeight(from: transition.Type, to: transition),
                expected: 1);
            Assert.Equal(
                actual: net.GetWeight(from: transition, to: transition.Type),
                expected: 1);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.FunctionTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Function_Transition_Creates_Arcs_With_Correct_Weights(
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
                type => Assert.Equal(
                    actual: net.GetWeight(from: type, to: transition),
                    expected: transition.ParameterTypes.Where(t => t.Equals(type)).Count()));
            Assert.Equal(
                actual: net.GetWeight(from: transition, to: transition.ReturnType),
                expected: 1);
        }

        [Theory]
        [MemberData(
            nameof(TestDataGenerator.VoidTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Add_Void_Transition_Creates_Arcs_With_Correct_Weights(VoidTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: transition, to: builder);
            builder.AddVoidType(_stubVoidType);

            // Act.
            builder.Add(transition);
            var net = builder.Build<FakePetriNet>();

            // Assert.
            Assert.All(
                transition.ParameterTypes,
                type => Assert.Equal(
                    actual: net.GetWeight(from: type, to: transition),
                    expected: transition.ParameterTypes.Where(t => t.Equals(type)).Count()));
            Assert.Equal(
                actual: net.GetWeight(from: transition, to: _stubVoidType),
                expected: 1);
        }

        // =========================================================================================
        // Private
        // =========================================================================================

        private void AddPlaces(CastTransition<FakePlace> from, Builder to)
        {
            to.Add(from.SubType);
            to.Add(from.SuperType);
        }

        private void AddPlaces(CloneTransition<FakePlace> from, Builder to) => to.Add(from.Type);

        private void AddPlaces(FunctionTransition<FakePlace> from, Builder to)
        {
            to.Add(from.ParameterTypes);
            to.Add(from.ReturnType);
        }

        private void AddPlaces(VoidTransition<FakePlace> from, Builder to) =>
            to.Add(from.ParameterTypes);

    }
}
