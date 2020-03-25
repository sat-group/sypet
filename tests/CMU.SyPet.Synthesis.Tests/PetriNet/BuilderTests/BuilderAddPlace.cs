using Xunit;
using static CMU.SyPet.Synthesis.PetriNetTests.TestDataGenerator;

namespace CMU.SyPet.Synthesis.PetriNetTests
{
    using Builder = PetriNetBuilder<FakePlace>;

    public class BuilderAddPlace
    {
        [Theory]
        [MemberData(
            nameof(PlaceTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Null_Error_Given_The_Place_Is_New(FakePlace place)
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
            nameof(PlaceTestDataGenerator),
            MemberType = typeof(TestDataGenerator))]
        public void Returns_Duplicate_Node_Error_Given_The_Place_Was_Already_Added(FakePlace place)
        {
            // Arrange.
            var builder = new PetriNetBuilder<FakePlace>();

            // Act.
            builder.Add(place);
            var error = builder.Add(place);

            // Assert.
            Assert.IsType<DuplicateNodeError>(error);
        }
    }    
}
