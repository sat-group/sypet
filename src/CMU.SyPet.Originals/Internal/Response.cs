namespace CMU.SyPet.Originals.Internals
{
    internal class Response
    {
        private Response(bool isCorrectProgram, Program program)
        {
            IsCorrectProgram = isCorrectProgram;
            Program = program;
        }

        public Response NewCorrectProgramResponse(Program program) => new Response(true, program);

        public Response NewWrongProgramResponse(Program program) => new Response(false, program);

        public bool IsCorrectProgram { get; }

        public Program Program { get; }
    }
}
