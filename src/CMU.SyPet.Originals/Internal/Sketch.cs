using System.Collections.Generic;

namespace CMU.SyPet.Originals.Internals
{
    internal class Sketch
    {
        public Sketch(IList<FunctionSignature> programLines)
        { ProgramLines = programLines; }

        public IList<FunctionSignature> ProgramLines;
    }
}
