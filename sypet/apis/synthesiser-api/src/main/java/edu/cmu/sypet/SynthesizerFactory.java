package edu.cmu.sypet;

import edu.cmu.sypet.java.LibraryName;
import edu.cmu.sypet.java.PackageName;
import java.util.Collection;

public interface SynthesizerFactory {

  Synthesizer createFrom(Collection<LibraryName> libraries, Collection<PackageName> packages);
}
