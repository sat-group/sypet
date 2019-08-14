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

public final class APTPetriNetAdapter implements PetriNet {

  private final uniol.apt.adt.pn.PetriNet delegate;

  private final Map<uniol.apt.adt.pn.Transition, APTPetriNetTransitionAdapter> transitionCache;
  private final Map<uniol.apt.adt.pn.Place, APTPetriNetPlaceAdapter> placeCache;
  private final Map<uniol.apt.adt.pn.Flow, APTPetriNetFlowAdapter> flowCache;

  private APTPetriNetAdapter(final uniol.apt.adt.pn.PetriNet petriNet) {
    this.delegate = petriNet;

    this.transitionCache = new HashMap<>();
    this.placeCache = new HashMap<>();
    this.flowCache = new HashMap<>();
  }

  public static APTPetriNetAdapter of(final uniol.apt.adt.pn.PetriNet petriNet) {
    return new APTPetriNetAdapter(petriNet);
  }

  @Override
  public Set<Transition> getTransitions() {
    return new HashSet<>(transitionCache.values());
  }

  @Override
  public boolean containsTransition(String transitionName) {
    return delegate.containsTransition(transitionName);
  }

  @Override
  public Transition createTransition(String transitionName) {
    final uniol.apt.adt.pn.Transition transition = delegate.createTransition(transitionName);
    final APTPetriNetTransitionAdapter transitionAdapter =
        new APTPetriNetTransitionAdapter(transition);

    this.transitionCache.put(transition, transitionAdapter);
    return transitionAdapter;
  }

  @Override
  public Set<Place> getPlaces() {
    return new HashSet<>(placeCache.values());
  }

  @Override
  public Place getPlace(String id) {
    try {
      return placeCache.get(delegate.getPlace(id));
    } catch (NoSuchNodeException e) {
      throw new NoSuchPlaceException(e);
    }
  }

  @Override
  public boolean containsPlace(String id) {
    return delegate.containsPlace(id);
  }

  @Override
  public void createPlace(String id) {
    final uniol.apt.adt.pn.Place place = delegate.createPlace(id);
    final APTPetriNetPlaceAdapter placeAdapter =
        new APTPetriNetPlaceAdapter(place);

    this.placeCache.put(place, placeAdapter);
  }

  @Override
  public void createFlow(String subclass, String methodName, int w) {
    final uniol.apt.adt.pn.Flow flow = delegate.createFlow(subclass, methodName, w);
    final APTPetriNetFlowAdapter flowAdapter =
        new APTPetriNetFlowAdapter(flow);

    this.flowCache.put(flow, flowAdapter);
  }

  @Override
  public void createFlow(Transition transition, Place place, int w) {
    this.createFlow(transition.getId(), place.getId(), w);
  }

  @Override
  public Flow getFlow(String id1, String id2) {
    try {
      return flowCache.get(delegate.getFlow(id1, id2));
    } catch (NoSuchEdgeException e) {
      throw new NoSuchFlowException(e);
    }
  }


  final class APTPetriNetTransitionAdapter implements Transition {

    private final uniol.apt.adt.pn.Transition delegate;

    private APTPetriNetTransitionAdapter(final uniol.apt.adt.pn.Transition transition) {
      this.delegate = transition;
    }

    @Override
    public String getId() {
      return delegate.getId();
    }

    @Override
    public Flow[] getPostsetEdges() {
      return delegate.getPostsetEdges().stream()
          .map(APTPetriNetAdapter.this.flowCache::get)
          .toArray(Flow[]::new);
    }

    @Override
    public Set<Flow> getPresetEdges() {
      return delegate.getPresetEdges().stream()
          .map(APTPetriNetAdapter.this.flowCache::get)
          .collect(Collectors.toSet());
    }

    @Override
    public String getLabel() {
      return delegate.getLabel();
    }
  }


  final class APTPetriNetFlowAdapter implements Flow {

    private final uniol.apt.adt.pn.Flow delegate;

    private APTPetriNetFlowAdapter(final uniol.apt.adt.pn.Flow flow) {
      this.delegate = flow;
    }

    @Override
    public Place getPlace() {
      return APTPetriNetAdapter.this.placeCache.get(delegate.getPlace());
    }

    @Override
    public Integer getWeight() {
      return delegate.getWeight();
    }

    @Override
    public void setWeight(int i) {
      delegate.setWeight(i);
    }

    @Override
    public Place getSource() {
      return APTPetriNetAdapter.this.placeCache.get(delegate.getSource());
    }
  }


  final class APTPetriNetPlaceAdapter implements Place {

    private final uniol.apt.adt.pn.Place delegate;

    private APTPetriNetPlaceAdapter(final uniol.apt.adt.pn.Place place) {
      this.delegate = place;
    }

    @Override
    public String getId() {
      return delegate.getId();
    }

    @Override
    public int getMaxToken() {
      return delegate.getMaxToken();
    }

    @Override
    public void setMaxToken(int i) {
      delegate.setMaxToken(i);
    }

    @Override
    public Set<Transition> getPostset() {
      return delegate.getPostset().stream()
          .map(APTPetriNetAdapter.this.transitionCache::get)
          .collect(Collectors.toSet());
    }

    @Override
    public Set<Transition> getPreset() {
      return delegate.getPreset().stream()
          .map(APTPetriNetAdapter.this.transitionCache::get)
          .collect(Collectors.toSet());
    }
  }

}
