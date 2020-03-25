using System;

namespace CMU.Commons.Types
{

    // Adapted from: https://mikhail.io/2016/01/validation-with-either-data-type-in-csharp/
    public class Either<TL, TR> : IEquatable<Either<TL, TR>>
    {
        private readonly object? _value;
        private readonly bool _isLeft;

        private Either(TL left)
        {
            _value = left;
            _isLeft = true;
        }

        private Either(TR right)
        {
            _value = right;
            _isLeft = false;
        }

        public static Either<TL, TR> Left(TL left) => new Either<TL, TR>(left);

        public static Either<TL, TR> Right(TR right) => new Either<TL, TR>(right);

        public static implicit operator Either<TL, TR>(TL left) => new Either<TL, TR>(left);

        public static implicit operator Either<TL, TR>(TR right) => new Either<TL, TR>(right);

        public T Match<T>(Func<TL, T> left, Func<TR, T> right) =>
            _isLeft ? left((TL)_value!) : right((TR)_value!);

        public void IfLeft(Action<TL> action)
        {
            if (_isLeft) action((TL)_value!);
        }

        public void IfRight(Action<TR> action)
        {
            if (!_isLeft) action((TR)_value!);
        }

        public bool Equals(Either<TL, TR> other) =>
            ((this._isLeft && other._isLeft) || (!this._isLeft && !other._isLeft))
                && _value!.Equals(other);

        public override bool Equals(object other) =>
            (other is Either<TL, TR>) && this.Equals((Either<TL, TR>)other);

        public override int GetHashCode()
        {
            return _value!.GetHashCode();
        }

        public override string ToString()
        {
            return string.Format("{0}({1})", _isLeft ? "Left" : "Right", _value!);
        }
    }
}
