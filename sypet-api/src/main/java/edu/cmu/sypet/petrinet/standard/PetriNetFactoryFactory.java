package edu.cmu.sypet.petrinet.standard;

import edu.cmu.sypet.java.MethodSignature;
import edu.cmu.sypet.java.Type;
import edu.cmu.sypet.petrinet.BackendPetriNet;
import java.util.function.Supplier;

public final class PetriNetFactoryFactory<T extends Type, MS extends MethodSignature<T>> implements
    edu.cmu.sypet.petrinet.PetriNetFactoryFactory<FrontendPetriNet<T, MS>, BackendPetriNet<T, MS>> {

  @Override
  public final edu.cmu.sypet.petrinet.PetriNetFactory<FrontendPetriNet<T, MS>> createFrom(
      final Supplier<BackendPetriNet<T, MS>> emptyNetSupplier
  ) {
    final BackendPetriNet<T, MS> net = emptyNetSupplier.get();
    final PetriNetBuilder<T, MS> builder = new PetriNetBuilder<>(net);

    return new PetriNetFactory<>(builder);
  }
}
