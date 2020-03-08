package edu.cmu.sypet.petrinet.simple;

// `PetriNetBuilder` director
public final class SyPetriNetFactory<T extends Type, MS extends MethodSignature<T>> {

  private final PetriNetBuilder<T, MS> builder;

  public SyPetriNetFactory(final PetriNetBuilder<T, MS> builder) {
    this.builder = builder;
  }

  public SyPetriNet<T, MS> createFrom(final Library library) {
      throw new UnsupportedOperationException();
//    try{
//    } catch (BadCastException e) {
//    } catch (NoSuchPlaceException e) {
//    } catch (TypeMismatchException e) {
//    }
  }
}
