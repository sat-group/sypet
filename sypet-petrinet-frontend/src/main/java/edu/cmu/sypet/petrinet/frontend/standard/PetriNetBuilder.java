package edu.cmu.sypet.petrinet.frontend.standard;

import com.rits.cloning.Cloner;
import edu.cmu.sypet.java.MethodSignature;
import edu.cmu.sypet.java.Type;
import edu.cmu.sypet.petrinet.backend.BackendPetriNet;
import java.util.Optional;
import java.util.stream.Stream;

final class PetriNetBuilder<T extends Type, MS extends MethodSignature<T>> {

  private final BackendPetriNet<T, MS> net;

  public PetriNetBuilder(final BackendPetriNet<T, MS> net) {
    this.net = net;
  }

  public final PetriNetBuilder<T, MS> addPlace(final T type) {
    // Ensure idempotence.
    if (!net.containsPlace(type)) {
      this.net.addPlace(type);
    }

    return this;
  }

  public PetriNetBuilder<T, MS> addTransition(final MS signature) {
    // Validate that all types in the signature are present in the net.
    validateTypesArePresent(signature.parametersTypes().stream());
    validateTypeIsPresent(signature.returnType());

    // Ensure idempotence.
    if (!net.containsTransition(signature)) {
      net.addTransition(signature);
    }

    return this;
  }

  private void validateTypesArePresent(Stream<T> types) {
    Optional<T> notFoundType = types
        .filter(type -> !net.containsPlace(type))
        .findAny();

    if (notFoundType.isPresent()) {
      throw new NoSuchPlaceException(notFoundType.get().name());
    }
  }

  private void validateTypeIsPresent(T type) {
    if (!net.containsPlace(type)) {
      throw new NoSuchPlaceException(type.name());
    }
  }

  final PetriNetBuilder<T, MS> addVoidTransition(final MS signature) {
    throw new UnsupportedOperationException();
  }

  final PetriNetBuilder<T, MS> addCloneTransition(final T type) {
    throw new UnsupportedOperationException();
  }

  final PetriNetBuilder<T, MS> addCastTransition(final T from, final T to) {
    throw new UnsupportedOperationException();
  }

  // this method should return a new copy of the private net
  public final FrontendPetriNet<T, MS> build() {
    BackendPetriNet<T, MS> copy = new Cloner().deepClone(this.net);
    return new FrontendPetriNet<>(copy);
  }
}
