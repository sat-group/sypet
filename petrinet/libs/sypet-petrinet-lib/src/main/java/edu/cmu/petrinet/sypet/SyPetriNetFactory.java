package edu.cmu.petrinet.sypet;

import java.util.Map.Entry;

// `PetriNetBuilder` director
public final class SyPetriNetFactory<T, U> {

  private final BackendPetriNet<Type<T>, MethodSignature<T, U>> backendPetriNet;

  public SyPetriNetFactory(BackendPetriNet<Type<T>, MethodSignature<T, U>> backendPetriNet) {
    this.backendPetriNet = backendPetriNet;
  }

  public SyPetriNet createFrom(final Library<T, U> library) {
    try {
      final PetriNetBuilder builder = new PetriNetBuilder(this.backendPetriNet);

      builder.addPlace(library.voidType());

      for (Type type : library.types()) {
        builder
            .addPlace(type)
            .addCloneTransition(type);
      }

      for (MethodSignature signature : library.signatures()) {
        builder
            .addTransition(signature)
            .addVoidTransition(signature, library.voidType());
      }

      for (Entry<Type<T>, Type<T>> entry : library.castRelation()) {
        final Type subtype = entry.getKey();
        final Type supertype = entry.getValue();

        builder.addCastTransition(subtype, supertype);
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
