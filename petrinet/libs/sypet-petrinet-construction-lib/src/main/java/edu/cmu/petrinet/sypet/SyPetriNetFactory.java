package edu.cmu.petrinet.sypet;

// `PetriNetBuilder` director
public final class SyPetriNetFactory {

  private final PetriNetBuilder builder;

  public SyPetriNetFactory(final PetriNetBuilder builder) {
    this.builder = builder;
  }

  public SyPetriNet createFrom(final Library library) {
      throw new UnsupportedOperationException();
//    try{
//    } catch (BadCastException e) {
//    } catch (NoSuchPlaceException e) {
//    } catch (TypeMismatchException e) {
//    }
  }
}
