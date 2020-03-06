package edu.cmu.sypet.petrinet;

import java.util.function.Supplier;

public interface PetriNetFactoryFactory<PNR extends FrontendPetriNet, PNW extends BackendPetriNet> {

  PetriNetFactory<PNR> createFrom(Supplier<PNW> emptyNetSupplier);
}
