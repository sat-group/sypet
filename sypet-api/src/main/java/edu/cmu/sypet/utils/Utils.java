package edu.cmu.sypet.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Utils {

  Utils() {
  }

  public static <T> Map<T, Set<T>> invertRelation(Map<T, Set<T>> relation) {
    // TODO Explain. It seems like we are "inverting" the map, so to speak.
    return relation.entrySet().stream()
        // Map each entry (class -> {superclass1, superclass2, ...}) to a map
        // { superclass1 -> {class}, superclass2 -> {class}, ... }.
        .map(
            entry ->
                entry.getValue().stream()
                    .collect(
                        Collectors.toMap(
                            Function.identity(), x -> ImmutableSet.of(entry.getKey()))))
        // Merge all maps in the stream by taking the union of their values whenever the keys are
        // equal.
        // For example, if we have two maps
        // { superclass1 -> {class1}, superclass2 -> {class1}, ... } and
        // { superclass1 -> {class2}, superclass2 -> {class2}, ... }, then the resulting merge
        // would be  { superclass1 -> {class1, class2}, superclass2 -> {class1, class2}, ... }.
        .flatMap(m -> m.entrySet().stream())
        .collect(
            Collectors.toMap(
                Entry::getKey, Entry::getValue, (set1, set2) -> Sets.union(set1, set2)));
  }


}
