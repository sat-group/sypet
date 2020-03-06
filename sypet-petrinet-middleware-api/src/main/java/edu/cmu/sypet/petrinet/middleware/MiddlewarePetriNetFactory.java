package edu.cmu.sypet.petrinet.middleware;

import edu.cmu.sypet.java.Library;

public interface MiddlewarePetriNetFactory<PNR extends MiddlewarePetriNet> {

  PNR createFrom(Library library);
}
