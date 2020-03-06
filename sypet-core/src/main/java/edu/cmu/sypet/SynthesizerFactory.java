package edu.cmu.sypet;

import java.util.Collection;

public interface SynthesizerFactory {

  Synthesizer createFrom(Collection<LibraryName> libraries, Collection<PackageName> packages);
}
