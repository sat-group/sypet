package edu.cmu.petrinet.sypet;

import static edu.cmu.petrinet.sypet.AdapterExtensions.newPlaceAdapter;
import static edu.cmu.petrinet.sypet.AdapterExtensions.newTransitionAdapter;

import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class PetriNetBuilder {

  private final BackendPetriNet net;

  public PetriNetBuilder(final BackendPetriNet net) {
    this.net = net;
  }

  public final PetriNetBuilder addPlace(final Type type) {
    PetriNetBuildException.handle(() -> this.net.addNode(newPlaceAdapter(type)));
    return this;
  }

  public final PetriNetBuilder addTransition(final MethodSignature signature) {
    PetriNetBuildException.handle(() -> {
      this.net.addNode(newTransitionAdapter(signature));

      // Connect the parameters types to the signature.
      // Count how many times a type appears in the parameters types and set that as the arc weight.
      Set<Entry<Type, Long>> entries = signature.parametersTypes().stream()
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
          .entrySet();

      for (Entry<Type, Long> entry : entries) {
        final Type type = entry.getKey();
        final Long count = entry.getValue();

        this.net.addArc(newPlaceAdapter(type), newTransitionAdapter(signature), count.intValue());
      }

      // Connect the signature to the return type.
      this.net.addArc(newTransitionAdapter(signature), newPlaceAdapter(signature.returnType()), 1);
    });

    return this;
  }

  public final PetriNetBuilder addCastTransition(final CastTransition transition) {
    throw new UnsupportedOperationException();
  }

  public final PetriNetBuilder addCloneTransition(final CloneTransition transition) {
    throw new UnsupportedOperationException();
  }

  public final PetriNetBuilder addVoidTransition(final VoidTransition transition) {
    throw new UnsupportedOperationException();
  }

  public final SyPetriNet build() {
    return new PetriNet(this.net);
  }
}