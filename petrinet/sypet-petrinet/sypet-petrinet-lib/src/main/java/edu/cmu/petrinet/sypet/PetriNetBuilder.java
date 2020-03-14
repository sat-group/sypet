package edu.cmu.petrinet.sypet;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class PetriNetBuilder {

  private final BackendPetriNet net;
  private VoidType voidType;

  public PetriNetBuilder(final BackendPetriNet net) {
    this.net = net;
  }

  public final PetriNetBuilder add(final Type type) {
    PetriNetBuildException.handle(() -> this.net.add(new PlaceAdapter(type)));
    return this;
  }

  public final PetriNetBuilder add(final VoidType type) {
    this.voidType = type;
    return this.add(type);
  }

  public final PetriNetBuilder add(final Iterable<Type> types) {
    for (Type type : types) {
      add(type);
    }
    return this;
  }

  public final PetriNetBuilder add(final MethodTransition transition) {
    PetriNetBuildException.handle(() -> {
      this.net.add(new TransitionAdapter(transition));
      addParametersTypes(transition.parametersTypes(), transition);
      this.net.add(new OutputArcAdapter(
          new TransitionAdapter(transition),
          new PlaceAdapter(transition.returnType()),
          1));
    });

    return this;
  }

  public final PetriNetBuilder add(final CastTransition transition) {
    PetriNetBuildException.handle(() -> {
      this.net.add(new TransitionAdapter(transition));
      this.net.add(new InputArcAdapter(
          new PlaceAdapter(transition.subtype()),
          new TransitionAdapter(transition),
          1));
      this.net.add(new OutputArcAdapter(
          new TransitionAdapter(transition),
          new PlaceAdapter(transition.supertype()),
          1));
    });

    return this;
  }

  public final PetriNetBuilder add(final CloneTransition transition) {
    PetriNetBuildException.handle(() -> {
      this.net.add(new TransitionAdapter(transition));
      this.net.add(new InputArcAdapter(
          new PlaceAdapter(transition.type()),
          new TransitionAdapter(transition),
          1));
      this.net.add(new OutputArcAdapter(
          new TransitionAdapter(transition),
          new PlaceAdapter(transition.type()),
          2));
    });

    return this;
  }

  public final PetriNetBuilder add(final VoidTransition transition) {
    if (this.voidType == null) {
      throw new PetriNetBuildException("Void type not set. You need to add the void type to "
          + "the Petri net before adding a void transition.");
    }

    PetriNetBuildException.handle(() -> {
      this.addParametersTypes(transition.parametersTypes(), transition);
      this.net.add(new OutputArcAdapter(
          new TransitionAdapter(transition),
          new PlaceAdapter(this.voidType),
          1));
    });

    return this;
  }

  public final SyPetriNet build() {
    return new PetriNet(this.net);
  }

  private void addParametersTypes(Collection<Type> parametersTypes, Transition transition)
      throws ArcAlreadyExistsException, NoSuchNodeException {
    // Count how many times a type appears in the parameters types and set that as the arc weight.
    Set<Entry<Type, Long>> entries = parametersTypes.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet();

    for (Entry<Type, Long> entry : entries) {
      final Type type = entry.getKey();
      final Long count = entry.getValue();

      this.net.add(new InputArcAdapter(
          new PlaceAdapter(type),
          new TransitionAdapter(transition),
          count.intValue()));
    }
  }
}