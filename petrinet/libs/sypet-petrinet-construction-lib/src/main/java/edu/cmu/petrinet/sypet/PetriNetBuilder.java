package edu.cmu.petrinet.sypet;

import com.rits.cloning.Cloner;
import java.util.Collection;
import java.util.Optional;
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
    final Type voidType = new TypeFactory().createVoidType();
    final MethodSignature voidSignature = new MethodSignature() {
      @Override
      public Collection<Type> parametersTypes() {
        return signature.parametersTypes();
      }

      @Override
      public String name() {
        return signature.name();
      }

      @Override
      public Type returnType() {
        return voidType;
      }
    };

    // Ensure idempotency.
    if (!this.net.containsTransition(voidSignature)) {
      this.net.addTransition(voidSignature);
    }

    throw new UnsupportedOperationException();
  }

  final PetriNetBuilder addCloneTransition(final Type type) {
    throw new UnsupportedOperationException();
  }

  final PetriNetBuilder addCastTransition(final Type from, final Type to) {
    throw new UnsupportedOperationException();
  }

  // this method should return a new copy of the private net
  public final SyPetriNet build() {
    BackendPetriNet<Type, MethodSignature> copy = new Cloner().deepClone(this.net);
    return new PetriNet(copy);
  }
}
