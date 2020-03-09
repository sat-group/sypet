package edu.cmu.petrinet.sypet;

import java.util.Map.Entry;

// `PetriNetBuilder` director
public final class SyPetriNetFactory {

  private final BackendPetriNet<Type, MethodSignature> backendPetriNet;

  public SyPetriNetFactory(BackendPetriNet<Type, MethodSignature> backendPetriNet) {
    this.backendPetriNet = backendPetriNet;
  }

  public SyPetriNet createFrom(final Library library) {
    try {
      final PetriNetBuilder builder = new PetriNetBuilder(this.backendPetriNet);

      builder.addVoid();

      for (Type type : library.types()) {
        builder
            .addPlace(type)
            .addCloneTransition(type);
      }

      for (MethodSignature signature : library.signatures()) {
        builder
            .addTransition(signature)
            .addVoidTransition(signature);
      }

      for (Entry<Type, Type> entry : library.castRelation()) {
        final Type subtype = entry.getKey();
        final Type supertype = entry.getValue();

        builder.addCastTransition(subtype, supertype);
      }

      return builder.build();
    } catch (NoSuchPlaceException e) {
      throw new PetriNetConstructionException(e);
    }
  }
}
