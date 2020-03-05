package edu.cmu.sypet.petrinet;

import java.util.function.Supplier;

public interface PetriNetFactoryFactory<PNR extends PetriNetRead, PNW extends PetriNetWrite> {

  PetriNetFactory<PNR, PNW> createFrom(Supplier<PNW> emptyNetSupplier);
}
