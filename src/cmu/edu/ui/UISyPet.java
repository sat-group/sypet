package cmu.edu.ui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sat4j.specs.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import cmu.edu.codeformer.CodeFormer;
import cmu.edu.compilation.Test;
import cmu.edu.parser.CacheMethodSignature;
import cmu.edu.parser.JarParser;
import cmu.edu.parser.JsonParser;
import cmu.edu.parser.MethodSignature;
import cmu.edu.parser.SyPetConfig;
import cmu.edu.petrinet.BuildNet;
import cmu.edu.reachability.Encoding;
import cmu.edu.reachability.EncodingUtil;
import cmu.edu.reachability.SequentialEncoding;
import cmu.edu.reachability.Variable;
import uniol.apt.adt.pn.PetriNet;

/**
 * This represents the UI for SyPet to be called by other applications.
 * 
 * @author Ruben Martins
 */
public class UISyPet {

	private List<MethodSignature> sigs;
	private Map<String, Set<String>> superclassMap;
	private Map<String, Set<String>> subclassMap;
	private List<String> packages;
	private List<String> libs;

	private PetriNet net;
	private Map<String, MethodSignature> signatureMap;

	private List<String> inputs;
	private String retType;
	private String testCode;
	private String methodName;
	private List<String> varNames;

	private BuildNet buildNet;

	public UISyPet(List<String> packages, List<String> libs) {

		//loadCache();
		
		this.packages = packages;
		this.libs = libs;
		
		String configPath = "config/config.json";
		SyPetConfig jsonConfig = new SyPetConfig(new ArrayList<>(), new ArrayList<>());		
		Path path = Paths.get(configPath);

		if (Files.exists(path))
			jsonConfig = JsonParser.parseJsonConfig(configPath); 
		
		Set<String> acceptableSuperClasses = new HashSet<>();
		acceptableSuperClasses.addAll(jsonConfig.acceptableSuperClasses);

		JarParser parser = new JarParser(libs);
		this.sigs = parser.parseJar(libs, packages, jsonConfig.blacklist);
		this.superclassMap = JarParser.getSuperClasses(acceptableSuperClasses);

		this.subclassMap = new HashMap<>();
		for (String key : superclassMap.keySet()) {
			for (String value : superclassMap.get(key)) {
				if (!subclassMap.containsKey(value)) {
					subclassMap.put(value, new HashSet<String>());
				}
				subclassMap.get(value).add(key);
			}
		}

		buildNet = new BuildNet();
		try {
			net = buildNet.build(sigs, superclassMap, subclassMap, new ArrayList<>(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		signatureMap = BuildNet.dict;
	}

	public void setSignature(String methodName, List<String> paramNames, List<String> srcTypes, String tgtType,
			String testCode) {

		this.inputs = srcTypes;
		this.retType = tgtType;
		this.testCode = testCode;
		this.varNames = paramNames;
		this.methodName = methodName;

		buildNet.setMaxTokens(srcTypes);
	}

	public String synthesize(int max_loc) {

		int loc = 1;
		boolean solution = false;
		String synthesizedCode = "";
		String code;

		while (!solution && loc <= max_loc) {
			// create a formula that has the same semantics as the petri-net
			Encoding encoding = new SequentialEncoding(net, loc);
			// set initial state and final state
			encoding.setState(EncodingUtil.setInitialState(net, inputs), 0);
			encoding.setState(EncodingUtil.setGoalState(net, retType), loc);

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
				CodeFormer former = new CodeFormer(signatures, inputs, retType, varNames, methodName, subclassMap,
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
						compre = Test.runTest(code, testCode, libs);
						//compre = Test.runTest(code, testCode);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (compre) {
						solution = true;
						synthesizedCode = code;
						break;
					}

					// the current path did not result in a program that passes all test cases find
					// the next path
					result = Encoding.solver.findPath(loc);
				}
			}

			// we did not find a program of length = loc
			loc++;
		}
		return synthesizedCode;

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
				CodeFormer former = new CodeFormer(signatures, inputs, retType, varNames, methodName, subclassMap,
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
						System.out.println("name= " + name);
						if(jsonReader.nextBoolean()) {
							name = "true";
						} else name = "false";
						pos = 0;
					} else pos++;
				} else if (pos == 1) {
					name = jsonReader.nextString();
					pos = 0;
				}

				System.out.println("name= " + name);
				
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
