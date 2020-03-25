using System;

namespace CMU.SyPet.Synthesis
{
    public class FakePlace : IEquatable<FakePlace>
    {
        public int Id { get; }

        public FakePlace(int id)
        {
            Id = id;
        }

        public bool Equals(FakePlace other)
        {
            return Id == other.Id;
        }

        public override bool Equals(object? obj)
        {
            return (obj is FakePlace) ? this.Equals((FakePlace)obj) : false;
        }

        public override int GetHashCode()
        {
            return Id.GetHashCode();
        }
    }
}
