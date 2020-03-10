package edu.cmu.petrinet.sypet;

import static edu.cmu.petrinet.sypet.AdapterExtensions.newPlace;
import static edu.cmu.petrinet.sypet.AdapterExtensions.newTransition;

import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

final class PetriNetBuilder<T, U> {

  private final BackendPetriNet<Type<T>, MethodSignature<T, U>> net;

  PetriNetBuilder(final BackendPetriNet<Type<T>, MethodSignature<T, U>> net) {
    this.net = net;
  }

  final PetriNetBuilder addPlace(final Type<T> type) throws PlaceAlreadyExistsException {
    this.net.addNode(newPlace(type));
    return this;
  }

  final PetriNetBuilder addTransition(final MethodSignature<T, U> signature) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException,
      TransitionAlreadyExistsException {
    this.net.addNode(newTransition(signature));

    // Connect the parameters types to the signature.
    // Count how many times a type appears in the parameters types and set that as the arc weight.
    Set<Entry<Type<T>, Long>> entries = signature.parametersTypes().stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet();

    for (Entry<Type<T>, Long> entry : entries) {
      final Type<T> type = entry.getKey();
      final Long count = entry.getValue();

      this.net.addArc(newPlace(type), newTransition(signature), count.intValue());
    }

    // Connect the signature to the return type.
    this.net.addArc(newTransition(signature), newPlace(signature.returnType()), 1);

    return this;
  }

  final PetriNetBuilder addVoidTransition(
      final MethodSignature<T, U> signature,
      final Type<T> voidType
  ) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException,
      TransitionAlreadyExistsException {
    return this.addTransition(new VoidMethodSignature(signature, voidType));
  }

  final PetriNetBuilder addCloneTransition(final Type type) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException,
      TransitionAlreadyExistsException {
    return this.addTransition(new CloneMethodSignature(type));
  }

  final PetriNetBuilder addCastTransition(final Type from, final Type to) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException,
      TransitionAlreadyExistsException {
    return this.addTransition(new CastMethodSignature(from, to));
  }

  final PetriNet build() {
    return new PetriNet(this.net);
  }
}