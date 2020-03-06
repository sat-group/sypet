package edu.cmu.sypet.petrinet.standard;

import edu.cmu.sypet.petrinet.PetriNetWrite;
import java.util.function.Supplier;

public final class PetriNetFactoryFactory<PNW extends PetriNetWrite> implements
    edu.cmu.sypet.petrinet.PetriNetFactoryFactory<PetriNetRead, PNW> {

  @Override
  public final edu.cmu.sypet.petrinet.PetriNetFactory<PetriNetRead> createFrom(
      final Supplier<PNW> emptyNetSupplier
  ) {
    final PNW net = emptyNetSupplier.get();
    final PetriNetBuilder builder = new PetriNetBuilder(net);

    return new PetriNetFactory(builder);
  }
}
