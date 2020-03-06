package edu.cmu.sypet.java;

import edu.cmu.sypet.LibraryName;
import edu.cmu.sypet.PackageName;
import java.util.Collection;

public interface LibraryFactory {

  Library createFrom(Collection<LibraryName> libraries, Collection<PackageName> packages);
}
