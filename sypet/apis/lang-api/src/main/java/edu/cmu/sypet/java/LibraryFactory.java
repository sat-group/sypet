package edu.cmu.sypet.java;

import java.util.Collection;

public interface LibraryFactory {

  Library createFrom(Collection<LibraryName> libraries, Collection<PackageName> packages);
}
