package edu.cmu.petrinet.sypet;

import static edu.cmu.petrinet.sypet.AdapterExtensions.newPlaceAdapter;
import static edu.cmu.petrinet.sypet.AdapterExtensions.newTransitionAdapter;

import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class PetriNetBuilder<T, U> {

  private final BackendPetriNet<Type<T>, MethodSignature<T, U>> net;

  public PetriNetBuilder(final BackendPetriNet<Type<T>, MethodSignature<T, U>> net) {
    this.net = net;
  }

  public final PetriNetBuilder<T, U> addPlace(final Type<T> type) throws PlaceAlreadyExistsException {
    this.net.addNode(newPlaceAdapter(type));
    return this;
  }

  public final PetriNetBuilder<T, U> addTransition(final MethodSignature<T, U> signature) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException,
      TransitionAlreadyExistsException {
    this.net.addNode(newTransitionAdapter(signature));

    // Connect the parameters types to the signature.
    // Count how many times a type appears in the parameters types and set that as the arc weight.
    Set<Entry<Type<T>, Long>> entries = signature.parametersTypes().stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet();

    for (Entry<Type<T>, Long> entry : entries) {
      final Type<T> type = entry.getKey();
      final Long count = entry.getValue();

      this.net.addArc(newPlaceAdapter(type), newTransitionAdapter(signature), count.intValue());
    }

    // Connect the signature to the return type.
    this.net.addArc(newTransitionAdapter(signature), newPlaceAdapter(signature.returnType()), 1);

    return this;
  }

  public final PetriNetBuilder<T, U> addCastTransition(final CastTransition<U> transition) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException,
      TransitionAlreadyExistsException {
    throw new UnsupportedOperationException();
  }

  public final PetriNetBuilder<T, U> addCloneTransition(final CloneTransition<U> transition) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException,
      TransitionAlreadyExistsException {
    throw new UnsupportedOperationException();
  }

  public final PetriNetBuilder<T, U> addVoidTransition(final VoidTransition<T, U> transition) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException,
      TransitionAlreadyExistsException {
    throw new UnsupportedOperationException();
  }

  public final SyPetriNet<T, U> build() {
    return new PetriNet(this.net);
  }
}