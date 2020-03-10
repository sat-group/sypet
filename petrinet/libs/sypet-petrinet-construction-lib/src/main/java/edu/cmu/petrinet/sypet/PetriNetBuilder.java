package edu.cmu.petrinet.sypet;

import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

final class PetriNetBuilder {

  private final BackendPetriNet<Type, MethodSignature> net;

  PetriNetBuilder(final BackendPetriNet<Type, MethodSignature> net) {
    this.net = net;
  }

  final PetriNetBuilder addPlace(final Type type) throws PlaceAlreadyExistsException {
    this.net.addPlace(type);
    return this;
  }

  final PetriNetBuilder addVoid() throws PlaceAlreadyExistsException {
    return this.addPlace(new TypeFactory().createVoidType());
  }

  final PetriNetBuilder addTransition(final MethodSignature signature) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException,
      TransitionAlreadyExistsException {
    this.net.addTransition(signature);

    // Connect the parameters types to the signature.
    // Count how many times a type appears in the parameters types and set that as the arc weight.
    Set<Entry<Type, Long>> entries = signature.parametersTypes().stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet();

    for (Entry<Type, Long> entry : entries) {
      final Type type = entry.getKey();
      final Long count = entry.getValue();

      this.net.addArcFromPlaceToTransition(type, signature, count.intValue());
    }

    // Connect the signature to the return type.
    this.net.addArcFromTransitionToPlace(signature, signature.returnType(), 1);

    return this;
  }

  final PetriNetBuilder addVoidTransition(final MethodSignature signature) throws
      ArcAlreadyExistsException,
      NoSuchPlaceException,
      NoSuchTransitionException,
      TransitionAlreadyExistsException {
    return this.addTransition(new VoidMethodSignature(signature));
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

  final SyPetriNet build() {
    return new PetriNet(this.net);
  }
}