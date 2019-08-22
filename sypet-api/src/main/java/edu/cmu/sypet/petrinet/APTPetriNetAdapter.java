package edu.cmu.sypet.petrinet;

import edu.cmu.reachability.petrinet.Flow;
import edu.cmu.reachability.petrinet.NoSuchFlowException;
import edu.cmu.reachability.petrinet.NoSuchPlaceException;
import edu.cmu.reachability.petrinet.PetriNet;
import edu.cmu.reachability.petrinet.Place;
import edu.cmu.reachability.petrinet.Transition;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import uniol.apt.adt.exception.NoSuchEdgeException;
import uniol.apt.adt.exception.NoSuchNodeException;

/**
 * This class is an adapter over the Petri net implementation from the APT library in order to make
 * it work with the Petri net interface from the reachability library.
 */
public final class APTPetriNetAdapter implements PetriNet {

  /**
   * The adaptee. Most methods are delegated to this underlying Petri net.
   */
  private final uniol.apt.adt.pn.PetriNet delegate;

  /**
   * Cache used in order to make sure there is a one-to-one correspondence between adapter and
   * adaptee.
   */
  private final Map<uniol.apt.adt.pn.Transition, APTPetriNetTransitionAdapter> transitionCache;

  /**
   * Cache used in order to make sure there is a one-to-one correspondence between adapter and
   * adaptee.
   */
  private final Map<uniol.apt.adt.pn.Place, APTPetriNetPlaceAdapter> placeCache;

  /**
   * Cache used in order to make sure there is a one-to-one correspondence between adapter and
   * adaptee.
   */
  private final Map<uniol.apt.adt.pn.Flow, APTPetriNetFlowAdapter> flowCache;

  /**
   * Single API to create a Petri net adapter.
   */
  public APTPetriNetAdapter() {
    this.delegate = new uniol.apt.adt.pn.PetriNet("delegate-petri-net");
    this.transitionCache = new HashMap<>();
    this.placeCache = new HashMap<>();
    this.flowCache = new HashMap<>();
  }

  /**
   * Get all the transitions in the Petri net.
   */
  @Override
  public Set<Transition> getTransitions() {
    return new HashSet<>(transitionCache.values());
  }

  /**
   * Creates a new transition with id {@code transitionId}, and places it in the cache.
   */
  @Override
  public Transition createTransition(final String transitionId) {
    final uniol.apt.adt.pn.Transition transition = delegate.createTransition(transitionId);
    final APTPetriNetTransitionAdapter transitionAdapter =
        new APTPetriNetTransitionAdapter(transition);

    this.transitionCache.put(transition, transitionAdapter);
    return transitionAdapter;
  }

  /**
   * Get all the places in the Petri net.
   */
  @Override
  public Set<Place> getPlaces() {
    return new HashSet<>(placeCache.values());
  }

  /**
   * Get the place with id {@code placeId} in the Petri net, or throw an exception if it cannot be
   * found.
   */
  @Override
  public Place getPlace(final String placeId) {
    try {
      return placeCache.get(delegate.getPlace(placeId));
    } catch (NoSuchNodeException e) {
      throw new NoSuchPlaceException(e);
    }
  }

  /**
   * Check whether there's a place with id {@code placeId} in the Petri net.
   */
  @Override
  public boolean containsPlace(final String placeId) {
    return delegate.containsPlace(placeId);
  }

  /**
   * Creates a new place with id {@code placeId}, and places it in the cache.
   */
  @Override
  public void createPlace(final String placeId) {
    final uniol.apt.adt.pn.Place place = delegate.createPlace(placeId);
    final APTPetriNetPlaceAdapter placeAdapter =
        new APTPetriNetPlaceAdapter(place);

    this.placeCache.put(place, placeAdapter);
  }

  /**
   * Creates a new flow with * weight {@code weight} from the node with id {@code from} to the node
   * with id {@code to}, and places it in the cache.
   */
  @Override
  public void createFlow(final String from, final String to, final int weight) {
    final uniol.apt.adt.pn.Flow flow = delegate.createFlow(from, to, weight);
    final APTPetriNetFlowAdapter flowAdapter =
        new APTPetriNetFlowAdapter(flow);

    this.flowCache.put(flow, flowAdapter);
  }

  /**
   * Returns the flow from the node with id {@code id1} to the node with id {@code id2}, or throws
   * an exception if it cannot be found. A node is either a place or a transition in the Petri net.
   */
  @Override
  public Flow getFlow(final String id1, final String id2) {
    try {
      return flowCache.get(delegate.getFlow(id1, id2));
    } catch (NoSuchEdgeException e) {
      throw new NoSuchFlowException(e);
    }
  }

  /**
   * This class is an adapter over the Petri net transition implementation from the APT library.
   */
  final class APTPetriNetTransitionAdapter implements Transition {

    /**
     * The adaptee.
     */
    private final uniol.apt.adt.pn.Transition delegate;

    private APTPetriNetTransitionAdapter(final uniol.apt.adt.pn.Transition transition) {
      this.delegate = transition;
    }

    /**
     * Returns the id of this transition.
     */
    @Override
    public String getId() {
      return delegate.getId();
    }

    /**
     * Returns the flows coming out of this transition.
     */
    @Override
    public Flow[] getPostsetEdges() {
      return delegate.getPostsetEdges().stream()
          .map(APTPetriNetAdapter.this.flowCache::get)
          .toArray(Flow[]::new);
    }

    /**
     * Returns the flows coming in to this transition.
     */
    @Override
    public Set<Flow> getPresetEdges() {
      return delegate.getPresetEdges().stream()
          .map(APTPetriNetAdapter.this.flowCache::get)
          .collect(Collectors.toSet());
    }

    /**
     * Returns the (optional) label of this transition.
     */
    @Override
    public String getLabel() {
      return delegate.getLabel();
    }
  }

  /**
   * This class is an adapter over the Petri net flow implementation from the APT library.
   */
  final class APTPetriNetFlowAdapter implements Flow {

    /**
     * The adaptee.
     */
    private final uniol.apt.adt.pn.Flow delegate;

    private APTPetriNetFlowAdapter(final uniol.apt.adt.pn.Flow flow) {
      this.delegate = flow;
    }

    /**
     * Returns the place of this flow.
     */
    @Override
    public Place getPlace() {
      return APTPetriNetAdapter.this.placeCache.get(delegate.getPlace());
    }

    /**
     * Returns the weight of this flow.
     */
    @Override
    public Integer getWeight() {
      return delegate.getWeight();
    }

    /**
     * Sets the weight of this flow.
     */
    @Override
    public void setWeight(final int i) {
      delegate.setWeight(i);
    }
  }

  /**
   * This class is an adapter over the Petri net place implementation from the APT library.
   */
  final class APTPetriNetPlaceAdapter implements Place {

    private final uniol.apt.adt.pn.Place delegate;

    private APTPetriNetPlaceAdapter(final uniol.apt.adt.pn.Place place) {
      this.delegate = place;
    }

    /**
     * Returns the id of this place.
     */
    @Override
    public String getId() {
      return delegate.getId();
    }

    @Override
    public int getMaxToken() {
      return delegate.getMaxToken();
    }

    @Override
    public void setMaxToken(final int i) {
      delegate.setMaxToken(i);
    }

    /**
     * Returns the transitions this place flows into.
     */
    @Override
    public Set<Transition> getPostset() {
      return delegate.getPostset().stream()
          .map(APTPetriNetAdapter.this.transitionCache::get)
          .collect(Collectors.toSet());
    }

    /**
     * Returns the transitions this place flows from.
     */
    @Override
    public Set<Transition> getPreset() {
      return delegate.getPreset().stream()
          .map(APTPetriNetAdapter.this.transitionCache::get)
          .collect(Collectors.toSet());
    }
  }

}
