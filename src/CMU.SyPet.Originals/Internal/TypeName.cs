using System;

namespace CMU.SyPet.Originals.Internals
{
    public class TypeName : IEquatable<TypeName>
    {
        public TypeName(string name)
        { Name = name; }

        public string Name { get; }

        public override bool Equals(object? other) => other is TypeName && this.Equals(other);

        public bool Equals(TypeName other) => Name.Equals(other.Name);

        public override int GetHashCode() => HashCode.Combine(Name);
    }
}
