namespace CMU.Commons.Types
{
    public sealed class Unit
    {
        public Unit() { }

        public override bool Equals(object obj) => obj is Unit;

        public override int GetHashCode() => 1;

        public override string ToString() => "Unit";
    }
}
