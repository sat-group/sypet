package edu.cmu.sypet.petrinet;

import edu.cmu.sypet.java.Library;

public interface PetriNetFactory<PNR extends PetriNetRead> {

  PNR createFrom(Library library);
}
