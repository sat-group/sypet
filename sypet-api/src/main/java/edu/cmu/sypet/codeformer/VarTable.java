package edu.cmu.sypet.codeformer;

import edu.cmu.sypet.java.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** Stores variable names specifically for CodeFormer. */
class VarTable {
  private final Map<Type, List<Integer>> table = new HashMap<>();
  private final Map<Integer, Type> lookupTable = new HashMap<>();

  public void addEntry(final Type type, final int var) {
    if (!table.containsKey(type)) {
      table.put(type, new ArrayList<>());
    }
    table.get(type).add(var);
    lookupTable.put(var, type);
  }

  /** No defensive copy is made here.
   * @param type*/
  public List<Integer> getEntries(final Type type) {
    if (table.containsKey(type)) return table.get(type);
    else return new LinkedList<>();
  }

  public Type getType(final int val) {
    return lookupTable.get(val);
  }
}
