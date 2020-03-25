using System.Linq;
using System.Collections.Generic;

namespace CMU.SyPet.Synthesis
{
    using Builder = PetriNetBuilder<FakePlace>;

    public static class TestDataGenerator
    {
        // =========================================================================================
        // Shorthand
        // =========================================================================================

        private static FakePlace FP(int id) => new FakePlace(id);

        private static CastTransition<FakePlace> CastT(FakePlace sub, FakePlace super) =>
            new CastTransition<FakePlace> { SubType = sub, SuperType = super };

        private static CloneTransition<FakePlace> CloneT(FakePlace type) =>
            new CloneTransition<FakePlace> { Type = type };

        private static FunctionTransition<FakePlace> FunT(FakePlace @out, params FakePlace[] ins) =>
            new FunctionTransition<FakePlace> { ParameterTypes = ins.ToList(), ReturnType = @out };

        private static VoidTransition<FakePlace> VoidT(params FakePlace[] ins) =>
            new VoidTransition<FakePlace> { ParameterTypes = ins.ToList() };

        // =========================================================================================
        // Data Generators
        // =========================================================================================

        private static IEnumerable<FakePlace> PlaceGenerator() => new[] { FP(0), FP(1) };

        private static IEnumerable<CastTransition<FakePlace>> CastTransitionGenerator() =>
            new List<CastTransition<FakePlace>>
            {
                CastT(sub: FP(0), super: FP(1)),
                CastT(sub: FP(0), super: FP(0))
            };

        private static IEnumerable<CloneTransition<FakePlace>> CloneTransitionGenerator() =>
            new List<CloneTransition<FakePlace>> { CloneT(type: FP(0)) };

        private static IEnumerable<FunctionTransition<FakePlace>> FunctionTransitionGenerator() =>
            new List<FunctionTransition<FakePlace>>
            {
                FunT(FP(3), FP(0), FP(1), FP(2)),
                FunT(FP(3), FP(0), FP(1), FP(2), FP(2)),
                FunT(FP(0), FP(0), FP(1)),
                FunT(FP(0), FP(0), FP(0)),
                FunT(FP(0), FP(1)),
                FunT(FP(0), FP(0)),
                FunT(FP(0))
            };

        private static IEnumerable<VoidTransition<FakePlace>> VoidTransitionGenerator() =>
            new List<VoidTransition<FakePlace>>
            {
                VoidT(FP(0), FP(1), FP(2)),
                VoidT(FP(0), FP(1), FP(2), FP(2)),
                VoidT(FP(0), FP(0), FP(0)),
                VoidT(FP(0), FP(1)),
                VoidT(FP(0), FP(0)),
                VoidT(FP(0)),
                VoidT()
            };

        // =========================================================================================
        // Test Data Generators
        // =========================================================================================

        public static IEnumerable<object[]> PlaceTestDataGenerator() =>
            PlaceGenerator().Select(node => new object[] { node });

        public static IEnumerable<object[]> CastTransitionTestDataGenerator() =>
            CastTransitionGenerator().Select(node => new object[] { node });

        public static IEnumerable<object[]> CloneTransitionTestDataGenerator() =>
            CloneTransitionGenerator().Select(node => new object[] { node });

        public static IEnumerable<object[]> FunctionTransitionTestDataGenerator() =>
            FunctionTransitionGenerator().Select(node => new object[] { node });

        public static IEnumerable<object[]> VoidTransitionTestDataGenerator() =>
            VoidTransitionGenerator().Select(node => new object[] { node });
    }
}
