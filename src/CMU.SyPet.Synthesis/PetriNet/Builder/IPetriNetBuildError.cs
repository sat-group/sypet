namespace CMU.SyPet.Synthesis
{
    public interface IPetriNetBuildError { }

    internal readonly struct DuplicateArcError : IPetriNetBuildError { }

    public readonly struct DuplicateNodeError : IPetriNetBuildError { }

    public readonly struct NoSuchNodeError : IPetriNetBuildError { }

    public readonly struct VoidTypeIsNotDefinedError : IPetriNetBuildError { }
}
