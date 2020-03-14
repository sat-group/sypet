package edu.cmu.petrinet.sypet;

import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;

// `PetriNetBuilder` director
public final class SyPetriNetFactory {

  private final BackendPetriNet backendPetriNet;
  private final BiFunction<Type, Type, CastTransition> newCastTransition;
  private final Function<Type, CloneTransition> newCloneTransition;
  private final Function<MethodSignature, VoidTransition> newVoidTransition;

  public SyPetriNetFactory(
      final BackendPetriNet backendPetriNet,
      BiFunction<Type, Type, CastTransition> newCastTransition,
      Function<Type, CloneTransition> newCloneTransition,
      Function<MethodSignature, VoidTransition> newVoidTransition) {
    this.backendPetriNet = backendPetriNet;
    this.newCastTransition = newCastTransition;
    this.newCloneTransition = newCloneTransition;
    this.newVoidTransition = newVoidTransition;
  }

  public SyPetriNet newSyPetriNet(final Library library) {
    final PetriNetBuilder builder = new PetriNetBuilder(this.backendPetriNet);

    builder.add(library.voidType());

    for (Type type : library.types()) {
      builder
          .add(type)
          .add(this.newCloneTransition.apply(type));
    }

    for (MethodSignature signature : library.signatures()) {
      builder
          .add(signature)
          .add(this.newVoidTransition.apply(signature));
    }

    for (Entry<Type, Type> entry : library.castRelation()) {
      final Type subtype = entry.getKey();
      final Type supertype = entry.getValue();

      builder.add(this.newCastTransition.apply(subtype, supertype));
    }

    return builder.build();
  }

}
