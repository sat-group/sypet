package edu.cmu.petrinet.sypet;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class PetriNetBuilder {

  private final BackendPetriNet<Type, MethodSignature> net;

  PetriNetBuilder(final BackendPetriNet<Type, MethodSignature> net) {
    this.net = net;
  }

  public final PetriNetBuilder addPlace(final Type type) {
    // Ensure idempotence.
    if (!this.net.containsPlace(type)) {
      this.net.addPlace(type);
    }

    return this;
  }

  public final PetriNetBuilder addVoid() {
    return this.addPlace(new TypeFactory().createVoidType());
  }

  public PetriNetBuilder addTransition(final MethodSignature signature)
      throws NoSuchTypeException {
    // Validate that all types in the signature are present in the net.
    validateTypesArePresent(signature.parametersTypes().stream(), signature);
    validateTypeIsPresent(signature.returnType(), signature);

    // Ensure idempotence.
    if (!this.net.containsTransition(signature)) {
      this.net.addTransition(signature);
    }

    // Connect the parameters types to the signature.
    // Count how many times a type appears in the parameters types and set that as the arc weight.
    signature.parametersTypes().stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet()
        .forEach(entry -> {
          final Type type = entry.getKey();
          final Long count = entry.getValue();

          if (!this.net.isPlaceAdjacentToTransition(type, signature)) {
            this.net.addArcFromPlaceToTransition(type, signature, count.intValue());
          }
        });

    // Connect the signature to the return type.
    // Ensure idempotence.
    if (!this.net.isTransitionAdjacentToPlace(signature, signature.returnType())) {
      this.net.addArcFromTransitionToPlace(signature, signature.returnType(), 1);
    }

    return this;
  }

  private void validateTypesArePresent(Stream<Type> types, MethodSignature signature)
      throws NoSuchTypeException {
    Optional<Type> notFoundType = types.filter(type -> !net.containsPlace(type)).findAny();

    if (notFoundType.isPresent()) {
      throw new NoSuchTypeException(notFoundType.get(), signature);
    }
  }

  private void validateTypeIsPresent(Type type, MethodSignature signature)
      throws NoSuchTypeException {
    validateTypesArePresent(Stream.of(type), signature);
  }

  final PetriNetBuilder addVoidTransition(final MethodSignature signature)
      throws NoSuchTypeException {
    return this.addTransition(new VoidMethodSignature(signature));
  }

  final PetriNetBuilder addCloneTransition(final Type type) throws NoSuchTypeException {
    return this.addTransition(new CloneMethodSignature(type));
  }

  final PetriNetBuilder addCastTransition(final Type from, final Type to)
      throws NoSuchTypeException {
    return this.addTransition(new CastMethodSignature(from, to));
  }

  // this method should return a new copy of the private net
  public final SyPetriNet build() {
    return new PetriNet(this.net);
  }
}