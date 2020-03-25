using System.Linq;
using Xunit;
using static CMU.SyPet.Synthesis.PetriNetTests.TestDataGenerator;
using static CMU.SyPet.Synthesis.PetriNetTests.TestUtils;

namespace CMU.SyPet.Synthesis.PetriNetTests
{
    using Builder = PetriNetBuilder<FakePlace>;
    
    public class BuilderAddFunctionTransition
    {
        [Theory]
        [MemberData(
            nameof(FunctionTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Null_Error_Given_The_Transition_Is_New(
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
            nameof(FunctionTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Duplicate_Node_Error_Given_The_Transition_Was_Already_Added(
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
            nameof(FunctionTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_No_Such_Node_Error_Given_At_Least_One_Parameter_Type_Was_Not_Added(
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
            nameof(FunctionTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_No_Such_Node_Error_Given_The_Return_Type_Was_Not_Added(
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
    }
}
