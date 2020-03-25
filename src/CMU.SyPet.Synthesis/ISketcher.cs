using System.Collections.Generic;

namespace CMU.SyPet.Synthesis
{
    public interface ISketcher<TSpec, TSketch, THint>
    {
        IEnumerable<TSketch> Sketch(TSpec spec);

        IEnumerable<TSketch> Sketch(TSpec spec, THint hint);
    }
}
