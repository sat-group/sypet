package edu.cmu.sypet.petrinet.adapters;

import edu.cmu.sypet.petrinet.PetriNet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import uniol.apt.adt.exception.NoSuchEdgeException;
import uniol.apt.adt.exception.NoSuchNodeException;

/**
 * Adapter over the Petri net implementation from the APT library in order to make it work with the
 * {@link edu.cmu.sypet.petrinet.PetriNet} interface.
 */
public class APTPetriNetAdapter implements PetriNet {

  //region Fields
  /** The adaptee. */
  private final uniol.apt.adt.pn.PetriNet petriNet;

  /**
   * Cache used in order to make sure there is a one-to-one correspondence between adapter and
   * adaptee.
   */
  private final Map<uniol.apt.adt.pn.Flow, Flow> flowCache;

  /**
   * Cache used in order to make sure there is a one-to-one correspondence between adapter and
   * adaptee.
   */
  private final Map<uniol.apt.adt.pn.Place, Place> placeCache;

  /**
   * Cache used in order to make sure there is a one-to-one correspondence between adapter and
   * adaptee.
   */
  private final Map<uniol.apt.adt.pn.Transition, Transition> transitionCache;
  //endregion

  //region Constructors
  APTPetriNetAdapter(
      uniol.apt.adt.pn.PetriNet petriNet,
      Map<uniol.apt.adt.pn.Flow, Flow> flowCache,
      Map<uniol.apt.adt.pn.Place, Place> placeCache,
      Map<uniol.apt.adt.pn.Transition, Transition> transitionCache
  ) {
    this.petriNet = petriNet;
    this.flowCache = flowCache;
    this.placeCache = placeCache;
    this.transitionCache = transitionCache;
  }

  public APTPetriNetAdapter(uniol.apt.adt.pn.PetriNet petriNet) {
    this(petriNet, new HashMap<>(), new HashMap<>(), new HashMap<>());
  }
  //endregion

  //region Methods
  /**
   * Get all the transitions in the Petri net.
   */
  @Override
  public Set<PetriNet.Transition> getTransitions() {
    return new HashSet<>(transitionCache.values());
  }

  /**
   * Creates a new transition with id {@code transitionId}, and places it in the cache.
   */
  @Override
  public PetriNet.Transition createTransition(final String transitionId) {
    final uniol.apt.adt.pn.Transition transition = petriNet.createTransition(transitionId);
    final Transition transitionAdapter = new Transition(transition);

    this.transitionCache.put(transition, transitionAdapter);

    return transitionAdapter;
  }

  /**
   * Get all the places in the Petri net.
   */
  @Override
  public Set<PetriNet.Place> getPlaces() {
    return new HashSet<>(placeCache.values());
  }

  /**
   * Get the place with id {@code placeId} in the Petri net, or throw an exception if it cannot be
   * found.
   */
  @Override
  public PetriNet.Place getPlace(final String placeId) {
    try {
      return placeCache.get(petriNet.getPlace(placeId));
    } catch (NoSuchNodeException e) {
      throw new NoSuchPlaceException(e);
    }
  }

  /**
   * Check whether there's a place with id {@code placeId} in the Petri net.
   */
  @Override
  public boolean containsPlace(final String placeId) {
    return petriNet.containsPlace(placeId);
  }

  /**
   * Creates a new place with id {@code placeId}, and places it in the cache.
   */
  @Override
  public void createPlace(final String placeId) {
    final uniol.apt.adt.pn.Place place = petriNet.createPlace(placeId);
    final Place placeAdapter = new Place(place);

    this.placeCache.put(place, placeAdapter);
  }

  /**
   * Creates a new flow with weight {@code weight} from the node with id {@code from} to the node
   * with id {@code to}, and places it in the cache.
   */
  @Override
  public void createFlow(final String from, final String to, final int weight) {
    final uniol.apt.adt.pn.Flow flow = petriNet.createFlow(from, to, weight);
    final Flow flowAdapter = new Flow(flow);

    this.flowCache.put(flow, flowAdapter);
  }

  /**
   * Returns the flow from the node with id {@code id1} to the node with id {@code id2}, or throws
   * an exception if it cannot be found. A node is either a place or a transition in the Petri net.
   */
  @Override
  public PetriNet.Flow getFlow(final String id1, final String id2) {
    try {
      return flowCache.get(petriNet.getFlow(id1, id2));
    } catch (NoSuchEdgeException e) {
      throw new NoSuchFlowException(e);
    }
  }
  //endregion

  //region Inner Classes
  /**
   * This class is an adapter over the Petri net transition implementation from the APT library.
   */
  final class Transition implements PetriNet.Transition {

    /**
     * The adaptee.
     */
    private final uniol.apt.adt.pn.Transition transition;

    private Transition(final uniol.apt.adt.pn.Transition transition) {
      this.transition = transition;
    }

    @Override
    public String getId() {
      return this.transition.getId();
    }

    @Override
    public String getLabel() {
      return this.transition.getLabel();
    }

    /**
     * Returns the flows coming in to this transition.
     */
    @Override
    public Set<PetriNet.Flow> getPresetEdges() {
      return this.transition.getPresetEdges().stream()
          .map(APTPetriNetAdapter.this.flowCache::get)
          .collect(Collectors.toSet());
    }

    @Override
    public Set<PetriNet.Flow> getPostsetEdges() {
      return this.transition.getPostsetEdges().stream()
          .map(APTPetriNetAdapter.this.flowCache::get)
          .collect(Collectors.toSet());
    }

  }

  /**
   * This class is an adapter over the Petri net flow implementation from the APT library.
   */
  final class Flow implements PetriNet.Flow {

    /**
     * The adaptee.
     */
    private final uniol.apt.adt.pn.Flow flow;

    private Flow(final uniol.apt.adt.pn.Flow flow) {
      this.flow = flow;
    }

    /**
     * Returns the place of this flow.
     */
    @Override
    public PetriNet.Place getPlace() {
      return APTPetriNetAdapter.this.placeCache.get(flow.getPlace());
    }

    /**
     * Returns the weight of this flow.
     */
    @Override
    public Integer getWeight() {
      return flow.getWeight();
    }

    /**
     * Sets the weight of this flow.
     */
    @Override
    public void setWeight(final int i) {
      flow.setWeight(i);
    }
  }

  /**
   * This class is an adapter over the Petri net place implementation from the APT library.
   */
  final class Place implements PetriNet.Place {

    private final uniol.apt.adt.pn.Place place;

    private Place(final uniol.apt.adt.pn.Place place) {
      this.place = place;
    }

    /**
     * Returns the id of this place.
     */
    @Override
    public String getId() {
      return place.getId();
    }

    @Override
    public int getMaxToken() {
      return place.getMaxToken();
    }

    @Override
    public void setMaxToken(final int i) {
      place.setMaxToken(i);
    }

    @Override
    public Set<PetriNet.Transition> getPostset() {
      return place.getPostset().stream()
          .map(Transition::new)
          .collect(Collectors.toSet());
    }

    @Override
    public Set<PetriNet.Transition> getPreset() {
      return place.getPreset().stream()
          .map(Transition::new)
          .collect(Collectors.toSet());
    }
  }
  //endregion

}
