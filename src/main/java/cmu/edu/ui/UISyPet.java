package cmu.edu.ui;

import cmu.edu.codeformer.CodeFormer;
import cmu.edu.compilation.Test;
import cmu.edu.parser.JarParser;
import cmu.edu.parser.JsonParser;
import cmu.edu.parser.MethodSignature;
import cmu.edu.parser.SyPetConfig;
import cmu.edu.parser.SyPetInput;
import cmu.edu.petrinet.BuildNet;
import cmu.edu.reachability.Encoding;
import cmu.edu.reachability.EncodingUtil;
import cmu.edu.reachability.SequentialEncoding;
import cmu.edu.reachability.Variable;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.sat4j.specs.TimeoutException;
import uniol.apt.adt.pn.PetriNet;

/**
 * This represents the UI for SyPet to be called by other applications.
 *
 * @author Ruben Martins
 */
public final class UISyPet {
  // TODO Use Collection instead of List.

  /**
   * TODO
   */
  private Map<String, Set<String>> superclassMap;

  /**
   * TODO
   */
  private Map<String, Set<String>> subclassMap;

  /**
   * TODO
   */
  private List<String> libs;

  /**
   * TODO
   */
  private PetriNet net;

  /**
   * TODO
   */
  private BuildNet buildNet;

  /**
   * TODO
   */
  private Map<String, MethodSignature> signatureMap;

  /**
   * TODO
   */
  private List<String> inputs;

  /**
   * TODO
   */
  private String retType;

  /**
   * TODO
   */
  private String testCode;

  /**
   * TODO
   */
  private String methodName;

  /**
   * TODO
   */
  private List<String> varNames;

  /**
   * TODO
   */
  private List<String> hints;

  /**
   * TODO
   */
  private List<MethodSignature> sigs;

  // TODO (Ruben) refactor this code
  // TODO Figure out why this exists. It is only used by the server.
  public UISyPet(List<String> packages, List<String> libs, List<String> soot, List<String> hints) {

    buildNet = null;
    net = null;
    sigs = null;
    this.hints = hints;

    this.libs = libs;

    String configPath = "./config/config.json";
    List<List<String>> globalSuperClasses = new ArrayList<>();
    ArrayList<String> poly1 = new ArrayList<String>(
        Arrays.asList("java.lang.CharSequence", "java.lang.String"));
    globalSuperClasses.add(poly1);
    SyPetConfig jsonConfig = new SyPetConfig.Builder()
        .globalSuperClasses(globalSuperClasses)
        .build();

    Path path = Paths.get(configPath);

    if (Files.exists(path)) {
      try {
        jsonConfig = JsonParser.parseJsonConfig(path);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    Set<String> localSuperClasses = new HashSet<>();
    localSuperClasses.addAll(jsonConfig.localSuperClasses);

    // suppress warnings from Soot
    PrintStream origOutput = System.out;
    PrintStream newOutput = new PrintStream(new ByteArrayOutputStream());
    System.setOut(newOutput);

    // TODO Review this block of code. I think this whole constructor is just an ad-hoc copy of the
    //  other one used solely for the server app.
    JarParser parser = new JarParser(soot, packages);
    this.sigs = parser.parseJar(jsonConfig.blacklist);
    this.superclassMap = parser.getSuperClasses(localSuperClasses);

    for (List<String> poly : jsonConfig.globalSuperClasses) {
      assert (poly.size() == 2);

      if (this.superclassMap.containsKey(poly.get(0))) {
        this.superclassMap.get(poly.get(0)).add(poly.get(1));
      } else {
        Set<String> subclass = new HashSet<>();
        subclass.add(poly.get(1));
        this.superclassMap.put(poly.get(0), subclass);
      }

    }
    this.subclassMap = new HashMap<>();
    for (String key : superclassMap.keySet()) {
      for (String value : superclassMap.get(key)) {
        if (!subclassMap.containsKey(value)) {
          subclassMap.put(value, new HashSet<String>());
        }
        subclassMap.get(value).add(key);
      }
    }

    System.setOut(origOutput);
    buildNet = new BuildNet(jsonConfig.noSideEffects);
    net = buildNet.build(sigs, superclassMap, subclassMap, new ArrayList<>(), true);
    signatureMap = BuildNet.dict;
    // System.out.println("c #Transitions = " + net.getTransitions().size());
    // System.out.println("c #Places = " + net.getPlaces().size());

  }

  public UISyPet(final List<String> packages, final List<String> libs, final List<String> hints,
      final SyPetConfig config) {
    this.libs = libs;
    this.hints = hints;

    // TODO Explain why we need this.
    // Suppress warnings from Soot.
    PrintStream origOutput = System.out;
    PrintStream newOutput = new PrintStream(new ByteArrayOutputStream());
    System.setOut(newOutput);

    final Set<String> localSuperClasses = new HashSet<>(config.localSuperClasses);
    final JarParser parser = new JarParser(this.libs, packages);

    // TODO Figure out why sigs must be parsed before we get the super classes.
    this.sigs = parser.parseJar(config.blacklist);

    // TODO Explain
    this.superclassMap = parser.getSuperClasses(localSuperClasses);

    // TODO Explain
    for (List<String> poly : config.globalSuperClasses) {
      // TODO If we only want arrays of size 2 we might be better using String[2], or a pair, or something else.
      assert (poly.size() == 2);

      if (this.superclassMap.containsKey(poly.get(0))) {
        this.superclassMap.get(poly.get(0)).add(poly.get(1));
      } else {
        Set<String> subclass = new HashSet<>();
        subclass.add(poly.get(1));
        this.superclassMap.put(poly.get(0), subclass);
      }
    }

    // TODO Explain. It seems like we are "inverting" the map, so to speak.
    this.subclassMap = new HashMap<>();
    for (String key : superclassMap.keySet()) {
      for (String value : superclassMap.get(key)) {
        if (!subclassMap.containsKey(value)) {
          subclassMap.put(value, new HashSet<String>());
        }
        subclassMap.get(value).add(key);
      }
    }

    // TODO Explain why we need this.
    System.setOut(origOutput);

    // TODO Why do we need both the petrinet and the factory?
    this.buildNet = new BuildNet(config.noSideEffects);
    this.net = this.buildNet.build(sigs, superclassMap, subclassMap, new ArrayList<>(), true);
    this.signatureMap = BuildNet.dict;

//		buildNet = new BuildNet(config.noSideEffects);
//		net = buildNet.build(sigs, superclassMap, subclassMap, new ArrayList<>(), true);
//		signatureMap = BuildNet.dict;

//		 System.out.println("c #Transitions = " + net.getTransitions().size());
//		 System.out.println("c #Places = " + net.getPlaces().size());

  }

  public UISyPet(SyPetInput input, SyPetConfig config) {
    this(input.packages, input.libs, input.hints, config);
  }

  public void setSignature(String methodName, List<String> paramNames, List<String> srcTypes,
      String tgtType,
      String testCode) {

    this.inputs = srcTypes;
    this.retType = tgtType;
    this.testCode = testCode;
    this.varNames = paramNames;
    this.methodName = methodName;

    buildNet.setMaxTokens(srcTypes);
  }

  public Optional<String> synthesize(int min_loc, int max_loc) {

    int loc = min_loc;
    boolean solution = false;
    String synthesizedCode = "";
    String code;
    int paths = 0;
    int programs = 0;

    while (!solution && loc <= max_loc) {
      System.out.println("c LOC = " + loc);
      // create a formula that has the same semantics as the petri-net
      Encoding encoding = new SequentialEncoding(net, loc);
      // set initial state and final state
      encoding.setState(EncodingUtil.setInitialState(net, inputs), 0);
      encoding.setState(EncodingUtil.setGoalState(net, retType), loc);
      encoding.setHints(hints);

      // 4. Perform reachability analysis

      // for each loc find all possible programs
      List<Variable> result = Encoding.solver.findPath(loc);
      paths++;
      while (!result.isEmpty() && !solution) {
        List<String> apis = new ArrayList<String>();
        // A list of method signatures
        List<MethodSignature> signatures = new ArrayList<>();
        for (Variable s : result) {
          apis.add(s.getName());
          MethodSignature sig = signatureMap.get(s.getName());
          if (sig != null) { // check if s is a line of a code
            signatures.add(sig);
          }
        }
        // 5. Convert a path to a program
        boolean sat = true;
        CodeFormer former = new CodeFormer(signatures, inputs, retType, varNames, methodName,
            subclassMap,
            superclassMap);
        while (sat) {
          try {
            code = former.solve();
            programs++;
          } catch (TimeoutException e) {
            sat = false;
            break;
          }
          sat = !former.isUnsat();
          // 6. Run the test cases
          boolean compre = false;
          // System.out.println("code = " + code);
          // System.out.println("testCode = " + testCode);
          try {
            compre = Test.runTest(code, testCode, libs);
          } catch (IOException e) {
            e.printStackTrace();
          }
          if (compre) {
            solution = true;
            synthesizedCode = code;
            break;
          }

        }
        // the current path did not result in a program that passes all test cases find the next path
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
    boolean solution = false;
    String code = "";

    while (!solution && loc <= max_loc) {
      // create a formula that has the same semantics as the petri-net
      Encoding encoding = new SequentialEncoding(net, loc);
      // set initial state and final state
      encoding.setState(EncodingUtil.setInitialState(net, inputs), 0);
      encoding.setState(EncodingUtil.setGoalState(net, retType), loc);
      encoding.setHints(hints);

      // 4. Perform reachability analysis

      // for each loc find all possible programs
      List<Variable> result = Encoding.solver.findPath(loc);
      while (!result.isEmpty() && !solution) {
        List<String> apis = new ArrayList<String>();
        // A list of method signatures
        List<MethodSignature> signatures = new ArrayList<>();
        for (Variable s : result) {
          apis.add(s.getName());
          MethodSignature sig = signatureMap.get(s.getName());
          if (sig != null) { // check if s is a line of a code
            signatures.add(sig);
          }
        }
        // 5. Convert a path to a program
        boolean sat = true;
        CodeFormer former = new CodeFormer(signatures, inputs, retType, varNames, methodName,
            subclassMap,
            superclassMap);
        while (sat) {
          try {
            code = former.solve();
          } catch (TimeoutException e) {
            sat = false;
            break;
          }
          sat = !former.isUnsat();
          // 6. Run the test cases
          boolean compre = false;
          try {
            compre = Test.runTest(code, testCode, this.libs);
          } catch (IOException e) {
            e.printStackTrace();
          }

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

  private void loadCache() {
    // CacheMethodSignature jobj = new
    // Gson().fromJson("./cache/cmu.symonster/cmu.symonster.json",
    // CacheMethodSignature.class);

    JsonReader jsonReader;
    try {
      jsonReader = new JsonReader(new FileReader("./cache/cmu.symonster/cmu.symonster.json"));

      jsonReader.beginObject();
      String name = jsonReader.nextName();
      jsonReader.beginArray();
      jsonReader.beginObject();
      int pos = 0;

      while (jsonReader.hasNext()) {

        if (pos == 0) {
          name = jsonReader.nextName();
          if (name.startsWith("is")) {
            //System.out.println("name= " + name);
            if (jsonReader.nextBoolean()) {
              name = "true";
            } else {
              name = "false";
            }
            pos = 0;
          } else {
            pos++;
          }
        } else if (pos == 1) {
          name = jsonReader.nextString();
          pos = 0;
        }

        //System.out.println("name= " + name);

      }

      jsonReader.endObject();
      jsonReader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
