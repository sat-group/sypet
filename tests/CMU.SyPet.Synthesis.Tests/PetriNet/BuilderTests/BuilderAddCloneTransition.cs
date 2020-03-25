using Xunit;
using static CMU.SyPet.Synthesis.PetriNetTests.TestDataGenerator;
using static CMU.SyPet.Synthesis.PetriNetTests.TestUtils;

namespace CMU.SyPet.Synthesis.PetriNetTests
{
    using Builder = PetriNetBuilder<FakePlace>;
    
    public class BuilderAddCloneTransition
    {
        [Theory]
        [MemberData(
            nameof(CloneTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Null_Error_Given_The_Transition_Is_New(
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
            nameof(CloneTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Duplicate_Node_Error_Given_The_Transition_Was_Already_Added(
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
            nameof(CloneTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_No_Such_Node_Error_Given_Type_Was_Not_Added(
            CloneTransition<FakePlace> transition)
        {
            // Arrange.
            var builder = new Builder();

            // Act.
            var error = builder.Add(transition);

            // Assert.
            Assert.IsType<NoSuchNodeError>(error);
        }
    }
}
