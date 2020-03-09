package edu.cmu.petrinet.sypet;

import com.rits.cloning.Cloner;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class PetriNetBuilder {

  private final BackendPetriNet<Type, MethodSignature> net;

  public PetriNetBuilder(final BackendPetriNet<Type, MethodSignature> net) {
    this.net = net;
  }

  public final PetriNetBuilder addPlace(final Type type) {
    // Ensure idempotence.
    if (!this.net.containsPlace(type)) {
      this.net.addPlace(type);
    }

    return this;
  }

  public PetriNetBuilder addTransition(final MethodSignature signature) {
    // Validate that all types in the signature are present in the net.
    validateTypesArePresent(signature.parametersTypes().stream());
    validateTypeIsPresent(signature.returnType());

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

  private void validateTypesArePresent(Stream<Type> types) {
    Optional<Type> notFoundType = types.filter(type -> !net.containsPlace(type)).findAny();

    if (notFoundType.isPresent()) {
      throw new NoSuchPlaceException(notFoundType.get().name());
    }
  }

  private void validateTypeIsPresent(Type type) {
    if (!this.net.containsPlace(type)) {
      throw new NoSuchPlaceException(type.name());
    }
  }

  final PetriNetBuilder addVoidTransition(final MethodSignature signature) {
    final MethodSignature voidSignature = new VoidMethodSignature(signature);

    this.addPlace(voidSignature.returnType());
    this.addTransition(voidSignature);

    return this;
  }

  final PetriNetBuilder addCloneTransition(final Type type) {
    final MethodSignature cloneSignature = new CloneMethodSignature(type);

    this.addTransition(cloneSignature);

    return this;
  }

  final PetriNetBuilder addCastTransition(final Type from, final Type to) {
    final MethodSignature castSignature = new CastMethodSignature(from, to);

    if (!from.isCastableTo(to)) {
      throw new BadCastException(from, to);
    }

    this.addTransition(castSignature);

    return this;
  }

  // this method should return a new copy of the private net
  public final SyPetriNet build() {
    BackendPetriNet<Type, MethodSignature> copy = new Cloner().deepClone(this.net);
    return new PetriNet(copy);
  }
}