using System.Linq;
using Xunit;
using static CMU.SyPet.Synthesis.PetriNetTests.TestUtils;
using static CMU.SyPet.Synthesis.PetriNetTests.TestDataGenerator;

namespace CMU.SyPet.Synthesis.PetriNetTests
{
    using Builder = PetriNetBuilder<FakePlace>;
    
    public class BuilderAddVoidTransition
    {
        [Theory]
        [MemberData(
            nameof(VoidTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Null_Error_Given_The_Transition_Is_New(
            VoidTransition<FakePlace> voidTransition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: voidTransition, to: builder);
            builder.AddVoidType(VoidTypeGenerator());

            // Act.
            var error = builder.Add(voidTransition);

            // Assert.
            Assert.Null(error);
        }

        [Theory]
        [MemberData(
            nameof(VoidTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Duplicate_Node_Error_Given_The_Transition_Was_Already_Added(
            VoidTransition<FakePlace> voidTransition)
        {
            // Arrange.
            var builder = new Builder();
            AddPlaces(from: voidTransition, to: builder);
            builder.AddVoidType(VoidTypeGenerator());

            // Act.
            builder.Add(voidTransition);
            var error = builder.Add(voidTransition);

            // Assert.
            Assert.IsType<DuplicateNodeError>(error);
        }

        [Theory]
        [MemberData(
            nameof(VoidTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_No_Such_Node_Error_Given_At_Least_One_Parameter_Type_Was_Not_Added(
            VoidTransition<FakePlace> transition)
        {
            // There are no unknown types if there are no parameter types to begin with.
            if (!transition.ParameterTypes.Any())
            { return; }

            // Arrange.
            var builder = new Builder();
            builder.AddVoidType(VoidTypeGenerator());

            // Act.
            var error = builder.Add(transition);

            // Assert.
            Assert.IsType<NoSuchNodeError>(error);
        }
    }
}
