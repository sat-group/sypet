package edu.cmu.sypet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import edu.cmu.sypet.codeformer.CodeFormer;
import edu.cmu.sypet.compilation.Test;
import edu.cmu.sypet.parser.MethodSignature;
import edu.cmu.sypet.parser.SootTypeFinder;
import edu.cmu.sypet.parser.TypeFinder;
import edu.cmu.sypet.petrinet.BuildNet;
import edu.cmu.sypet.reachability.Encoding;
import edu.cmu.sypet.reachability.EncodingUtil;
import edu.cmu.sypet.reachability.SequentialEncoding;
import edu.cmu.sypet.reachability.Variable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.sat4j.specs.TimeoutException;
import uniol.apt.adt.pn.PetriNet;

/**
 * This class represents the SyPet library API.
 */
@SuppressWarnings("WeakerAccess")
public final class SyPetAPI {
  // TODO Use Collection instead of List.

  private final SynthesisTask task;

  private final Map<String, Set<String>> superclassMap;
  private final Map<String, Set<String>> subclassMap;
  private final PetriNet net;
  private final Map<String, MethodSignature> signatureMap;

  public SyPetAPI(final SynthesisTask task) throws SyPetException {
    this.task = task;

    final List<MethodSignature> signatures;
    try (final TypeFinder typeFinder = new SootTypeFinder(getLibs(), task.packages())) {
      final Set<String> localSuperClasses = new HashSet<>(task.localSuperClasses());

      this.superclassMap = typeFinder.getSuperClasses(localSuperClasses, task.packages());
      this.subclassMap = invertRelation(getSuperclassMap());

      signatures = typeFinder.getSignatures(task.blacklist());
    } catch (Exception e) {
      throw new SyPetException(e);
    }

    BuildNet buildNet = new BuildNet(task.noSideEffects());
    this.net =
        buildNet.build(signatures, getSuperclassMap(), getSubclassMap(), new ArrayList<>(), true);
    this.signatureMap = BuildNet.dict;
  }

  /**
   * Synthesize a program from a <code>SynthesisTask</code>.
   *
   * @param synthesisTask the parameters to the synthesis task
   * @return optionally a program, if one can be synthesized
   * @see SynthesisTask
   */
  public static Optional<String> synthesize(SynthesisTask synthesisTask) throws SyPetException {
    final SyPetAPI sypet = new SyPetAPI(synthesisTask);
    return sypet.synthesize(synthesisTask.locLowerBound(), synthesisTask.locUpperBound());
  }

  private static <T> Map<T, Set<T>> invertRelation(Map<T, Set<T>> relation) {
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

  private Optional<String> synthesize(int min_loc, int max_loc) {

    int loc = min_loc;
    boolean solution = false;
    String synthesizedCode = "";
    String code;
    int paths = 0;
    int programs = 0;

    while (!solution && loc <= max_loc) {
      System.out.println("c LOC = " + loc);
      // create a formula that has the same semantics as the petri-net
      Encoding encoding = new SequentialEncoding(getNet(), loc);
      // set initial state and final state
      encoding.setState(EncodingUtil.setInitialState(getNet(), getParamTypes()), 0);
      encoding.setState(EncodingUtil.setGoalState(getNet(), getReturnType()), loc);
      encoding.setHints(getHints());

      // 4. Perform reachability analysis

      // for each loc find all possible programs
      List<Variable> result = Encoding.solver.findPath(loc);
      paths++;
      while (!result.isEmpty() && !solution) {
        List<String> apis = new ArrayList<>();
        // A list of method signatures
        List<MethodSignature> signatures = new ArrayList<>();
        for (Variable s : result) {
          apis.add(s.getName());
          MethodSignature sig = getSignatureMap().get(s.getName());
          if (sig != null) { // check if s is a line of a code
            signatures.add(sig);
          }
        }
        // 5. Convert a path to a program
        boolean sat = true;
        CodeFormer former =
            new CodeFormer(
                signatures,
                getParamTypes(),
                getReturnType(),
                getParamNames(),
                getMethodName(),
                getSubclassMap(),
                getSuperclassMap());
        while (sat) {
          try {
            code = former.solve();
            programs++;
          } catch (TimeoutException e) {
            break;
          }
          sat = !former.isUnsat();
          // 6. Run the test cases
          boolean compre;
          // System.out.println("code = " + code);
          // System.out.println("testCode = " + testCode);
          compre = Test.runTest(code, getTestCode(), getLibs());
          if (compre) {
            solution = true;
            synthesizedCode = code;
            break;
          }
        }
        // the current path did not result in a program that passes all test cases find the next
        // path
        paths++;
        result = Encoding.solver.findPath(loc);
      }

      // we did not find a program of length = loc
      loc++;
    }
    System.out.println("c #Programs explored = " + programs);
    System.out.println("c #Paths explored = " + paths);

    return Optional.of(synthesizedCode).filter(s -> !s.isEmpty());
  }

  public List<String> synthesizeAll(int max_loc) {
    ArrayList<String> allCode = new ArrayList<>();

    int loc = 1;
    String code;

    while (loc <= max_loc) {
      // create a formula that has the same semantics as the petri-net
      Encoding encoding = new SequentialEncoding(getNet(), loc);
      // set initial state and final state
      encoding.setState(EncodingUtil.setInitialState(getNet(), getParamTypes()), 0);
      encoding.setState(EncodingUtil.setGoalState(getNet(), getReturnType()), loc);
      encoding.setHints(getHints());

      // 4. Perform reachability analysis

      // for each loc find all possible programs
      List<Variable> result = Encoding.solver.findPath(loc);
      while (!result.isEmpty()) {
        List<String> apis = new ArrayList<>();
        // A list of method signatures
        List<MethodSignature> signatures = new ArrayList<>();
        for (Variable s : result) {
          apis.add(s.getName());
          MethodSignature sig = getSignatureMap().get(s.getName());
          if (sig != null) { // check if s is a line of a code
            signatures.add(sig);
          }
        }
        // 5. Convert a path to a program
        boolean sat = true;
        CodeFormer former =
            new CodeFormer(
                signatures,
                getParamTypes(),
                getReturnType(),
                getParamNames(),
                getMethodName(),
                getSubclassMap(),
                getSuperclassMap());
        while (sat) {
          try {
            code = former.solve();
          } catch (TimeoutException e) {
            break;
          }
          sat = !former.isUnsat();
          // 6. Run the test cases
          boolean compre;
          compre = Test.runTest(code, getTestCode(), this.getLibs());

          if (compre) {
            allCode.add(code);
          }

          // the current path did not result in a program that passes all test cases find
          // the next path
          result = Encoding.solver.findPath(loc);
        }
      }

      // we did not find a program of length = loc
      loc++;
    }
    return allCode;
  }

  public Map<String, Set<String>> getSuperclassMap() {
    return superclassMap;
  }

  public Map<String, Set<String>> getSubclassMap() {
    return subclassMap;
  }

  public PetriNet getNet() {
    return net;
  }

  public Map<String, MethodSignature> getSignatureMap() {
    return signatureMap;
  }

  public String getMethodName() {
    return this.task.methodName();
  }

  public List<String> getParamNames() {
    return this.task.paramNames();
  }

  public List<String> getParamTypes() {
    return this.task.paramTypes();
  }

  public String getReturnType() {
    return this.task.returnType();
  }

  public List<String> getLibs() {
    return this.task.libs();
  }

  public String getTestCode() {
    return this.task.testCode();
  }

  public List<String> getHints() {
    return this.task.hints();
  }
}
