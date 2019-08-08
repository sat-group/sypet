package edu.cmu.sypet.reachability;

/**
 * Variable representation for the constraint solver.
 *
 * @author Ruben Martins
 */
public class Variable implements Comparable<Variable> {

  private final int id;
  private final String name;
  private final int timestep;
  private final int value;
  private final Type type;
  private final int flows;

  public Variable(final int id, final String name, final Type type) {
    this.id = id;
    this.name = name;
    this.timestep = 0;
    this.value = 0;
    this.type = type;
    this.flows = 0;
  }

  public Variable(final int id, final String name, final Type type, final int timestep) {
    this.id = id;
    this.name = name;
    this.timestep = timestep;
    this.value = 0;
    this.type = type;
    this.flows = 0;
  }

  public Variable(final int id, final String name, final Type type, final int timestep,
      final int value) {
    this.id = id;
    this.name = name;
    this.timestep = timestep;
    this.value = value;
    this.type = type;
    this.flows = 0;
  }

  public Variable(final int id, final String name, final Type type, final int timestep,
      final int value, final int flows) {
    this.id = id;
    this.name = name;
    this.timestep = timestep;
    this.value = value;
    this.type = type;
    this.flows = flows;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getTimestep() {
    return timestep;
  }

  public int getValue() {
    return value;
  }

  public Type getType() {
    return type;
  }

  @Override
  public String toString() {
    String res = "";
    if (type == Type.TRANSITION) {
      res += "[" + name + "]" + "(id=" + id + ",Type=" + type + ",timestep=" + timestep + ")";
    } else if (type == Type.PLACE) {
      res +=
          "["
              + name
              + "]"
              + "(id="
              + id
              + ",Type="
              + type
              + ",timestep="
              + timestep
              + ",value="
              + value
              + ")";
    } else if (type == Type.FLOWPLACE) {
      res +=
          "["
              + name
              + "]"
              + "(id="
              + id
              + ",Type="
              + type
              + ",id="
              + value
              + ",flows="
              + flows
              + ",timestep="
              + timestep
              + ")";
    }

    return res;
  }

  @Override
  public int compareTo(final Variable var) {
    return this.timestep - var.timestep;
  }

  enum Type {
    FLOWPLACE,
    PLACE,
    TRANSITION
  }
}
