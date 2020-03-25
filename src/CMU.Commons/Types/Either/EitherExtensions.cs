using System;
using System.Collections.Generic;

namespace CMU.Commons.Types
{
    public static class EitherExtensions
    {
        public static Either<TL, TR> Cast<TL, TR, T>(this Either<TL, T> either) =>
            either.Match(
                left: x => x,
                right: _ => throw new InvalidOperationException("The Either is not a left")
            );

        public static Either<TL, TR> Cast<TL, TR, T>(this Either<T, TR> either) =>
            either.Match(
                left: _ => throw new InvalidOperationException("The Either is not a right."),
                right: x => x
            );

        public static Either<T, V> Bind<T, U, V>(this Either<T, U> e, Func<U, Either<T, V>> f) =>
            e.Match(left: x => x, right: f);

        public static Either<TL, IEnumerable<TR>> Sequence<TL, TR>(
            this IEnumerable<Either<TL, TR>> eithers)
        {
            var results = new List<TR>();
            Either<TL, IEnumerable<TR>>? error = null;

            foreach (var e in eithers)
            {
                e.IfLeft(x => error = x);
                e.IfRight(x => results.Add(x));

                if (error != null)
                {
                    return error;
                }
            }

            return results;
        }

        public static Either<TL, Unit> Sequence_<TL, TR>(
            this IEnumerable<Either<TL, TR>> eithers)
        {
            Either<TL, Unit>? error = null;

            foreach (var e in eithers)
            {
                e.IfLeft(x => error = x);

                if (error != null)
                {
                    return error;
                }
            }

            return new Unit();
        }
    }
}
