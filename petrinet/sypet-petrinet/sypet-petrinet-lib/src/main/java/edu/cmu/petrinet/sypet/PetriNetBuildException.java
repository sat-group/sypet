package edu.cmu.petrinet.sypet;

public class PetriNetBuildException extends RuntimeException {

  PetriNetBuildException(final String message) {
    super("Internal error while building the Petri net: " + message);
  }

  PetriNetBuildException(final ArcAlreadyExistsException e) {
    this("Arc from \"" + e.getArc().getFrom() + "\" to \"" + e.getArc().getTo()
        + "\" already exists");
  }

  PetriNetBuildException(final NoSuchNodeException e) {
    this("Node \"" + e.getNode() + "\" does not exist.");
  }

  PetriNetBuildException(final NodeAlreadyExistsException e) {
    this("Node \"" + e.getNode() + "\" already exists.");
  }

  static void handle(final Block block) throws PetriNetBuildException {
    try {
      block.run();
    } catch (ArcAlreadyExistsException e) {
      throw new PetriNetBuildException(e);
    } catch (NoSuchNodeException e) {
      throw new PetriNetBuildException(e);
    } catch (NodeAlreadyExistsException e) {
      throw new PetriNetBuildException(e);
    }
  }

  interface Block {

    void run() throws
        ArcAlreadyExistsException,
        NoSuchNodeException,
        NodeAlreadyExistsException;
  }
}
