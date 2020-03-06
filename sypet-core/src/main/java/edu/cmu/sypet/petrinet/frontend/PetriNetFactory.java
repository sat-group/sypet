package edu.cmu.sypet.petrinet.frontend;

import edu.cmu.sypet.java.Library;

public interface PetriNetFactory<PNR extends FrontendPetriNet> {

  PNR createFrom(Library library);
}
