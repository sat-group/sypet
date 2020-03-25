using Xunit;
using static CMU.SyPet.Synthesis.PetriNetTests.TestDataGenerator;
using static CMU.SyPet.Synthesis.PetriNetTests.TestUtils;


namespace CMU.SyPet.Synthesis.PetriNetTests
{
    using Builder = PetriNetBuilder<FakePlace>;
    
    public class BuilderAddCastTransition
    {
        [Theory]
        [MemberData(
            nameof(CastTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Null_Error_Given_The_Transition_Is_New(
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
            nameof(CastTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Duplicate_Node_Error_Given_The_Transition_Was_Already_Added(
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
            nameof(CastTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_No_Such_Node_Error_Given_SubType_Was_Not_Added(
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
            nameof(CastTransitionTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_No_Such_Node_Error_Given_SuperType_Was_Not_Added(
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
    }
}
