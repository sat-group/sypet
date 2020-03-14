package edu.cmu.petrinet.sypet;

public class PetriNetBuildException extends RuntimeException {

  PetriNetBuildException(final String message) {
    super("Internal error while building the Petri net: " + message);
  }

  private PetriNetBuildException(final ArcAlreadyExistsException e) {
    this("Arc from \"" + e.source + "\" to \"" + e.target + "\" already exists");
  }

  private PetriNetBuildException(final NoSuchArcException e) {
    this("No arc from \"" + e.source + "\" to target \"" + e.target + "\" exists.");
  }

  private PetriNetBuildException(final NoSuchPlaceException e) {
    this("Place \"" + e.place + "\" does not exist in the net.");
  }

  private PetriNetBuildException(final NoSuchTransitionException e) {
    this("Transition \"" + e.transition + "\" does not exist in the net.");
  }

  private PetriNetBuildException(final PlaceAlreadyExistsException e) {
    this("Place \"" + e.place + "\" already exists in the net.");
  }

  private PetriNetBuildException(final TransitionAlreadyExistsException e) {
    this("Transition \"" + e.transition + "\" already exists in the net.");
  }

  static void handle(final Block block) throws PetriNetBuildException {
    try {
      block.run();
    } catch (ArcAlreadyExistsException e) {
      throw new PetriNetBuildException(e);
    } catch (NoSuchArcException e) {
      throw new PetriNetBuildException(e);
    } catch (NoSuchPlaceException e) {
      throw new PetriNetBuildException(e);
    } catch (NoSuchTransitionException e) {
      throw new PetriNetBuildException(e);
    } catch (PlaceAlreadyExistsException e) {
      throw new PetriNetBuildException(e);
    } catch (TransitionAlreadyExistsException e) {
      throw new PetriNetBuildException(e);
    }
  }

  interface Block {
    void run() throws
        ArcAlreadyExistsException,
        NoSuchArcException,
        NoSuchPlaceException,
        NoSuchTransitionException,
        PlaceAlreadyExistsException,
        TransitionAlreadyExistsException;
  }
}
