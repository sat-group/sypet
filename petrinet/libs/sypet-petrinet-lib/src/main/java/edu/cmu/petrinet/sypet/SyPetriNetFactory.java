package edu.cmu.petrinet.sypet;

import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;

// `PetriNetBuilder` director
public final class SyPetriNetFactory<T, U> {

  private final BackendPetriNet<Type<T>, MethodSignature<T, U>> backendPetriNet;
  private final BiFunction<Type<T>, Type<T>, CastTransition<U>> newCastTransition;
  private final Function<Type<T>, CloneTransition<U>> newCloneTransition;
  private final Function<MethodSignature<T, U>, VoidTransition<T, U>> newVoidTransition;

  public SyPetriNetFactory(
      final BackendPetriNet<Type<T>, MethodSignature<T, U>> backendPetriNet,
      BiFunction<Type<T>, Type<T>, CastTransition<U>> newCastTransition,
      Function<Type<T>, CloneTransition<U>> newCloneTransition,
      Function<MethodSignature<T, U>, VoidTransition<T, U>> newVoidTransition) {
    this.backendPetriNet = backendPetriNet;
    this.newCastTransition = newCastTransition;
    this.newCloneTransition = newCloneTransition;
    this.newVoidTransition = newVoidTransition;
  }

  public SyPetriNet createFrom(final Library<T, U> library) {
    try {
      final PetriNetBuilder builder = new PetriNetBuilder(this.backendPetriNet);

      builder.addPlace(library.voidType());

      for (Type type : library.types()) {
        builder
            .addPlace(type)
            .addCloneTransition(this.newCloneTransition.apply(type));
      }

      for (MethodSignature signature : library.signatures()) {
        builder
            .addTransition(signature)
            .addVoidTransition(this.newVoidTransition.apply(signature));
      }

      for (Entry<Type<T>, Type<T>> entry : library.castRelation()) {
        final Type subtype = entry.getKey();
        final Type supertype = entry.getValue();

        builder.addCastTransition(this.newCastTransition.apply(subtype, supertype));
      }

      return builder.build();
    } catch (ArcAlreadyExistsException
        | NoSuchPlaceException
        | NoSuchTransitionException
        | PlaceAlreadyExistsException
        | TransitionAlreadyExistsException e) {
      throw new PetriNetBuildException(e);
    }
  }

}
