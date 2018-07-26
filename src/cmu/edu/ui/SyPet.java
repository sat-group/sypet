/**
 * BSD 3-Clause License
 *	
 *	
 *	Copyright (c) 2018, SyPet 2.0 - Ruben Martins, Yu Feng, Isil Dillig
 *	All rights reserved.
 *	
 *	Redistribution and use in source and binary forms, with or without
 *	modification, are permitted provided that the following conditions are met:
 *	
 *	* Redistributions of source code must retain the above copyright notice, this
 *	  list of conditions and the following disclaimer.
 *	
 *	* Redistributions in binary form must reproduce the above copyright notice,
 *	  this list of conditions and the following disclaimer in the documentation
 *	  and/or other materials provided with the distribution.
 *	
 *	* Neither the name of the copyright holder nor the names of its
 *	  contributors may be used to endorse or promote products derived from
 *	  this software without specific prior written permission.
 *	
 *	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *	AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *	IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *	DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 *	FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *	DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *	SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *	CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *	OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *	OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package cmu.edu.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sat4j.specs.TimeoutException;

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
import cmu.edu.utils.TimerUtils;
import uniol.apt.adt.pn.PetriNet;

/**
 * This represents the main entry point to synthesis loop from SyPet.
 * 
 * @author Ruben Martins
 * @author Kaige Liu
 * @author Anlun Xu
 * @author Yu Feng
 */
public class SyPet {

	public static void main(String[] args) throws IOException {

		// Command line arguments
		List<String> arglist = Arrays.asList(args);
		boolean clone = true;
		boolean equiv = true;
		boolean copyPoly = true;
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));

		if (args.length != 1) {
			System.out.println("Error: wrong number of arguments= " + args.length);
			System.out.println("Usage: ./sypet.sh <filename.json>");
			System.exit(0);
		}
		
		Path jsonFilePath = Paths.get(args[0]);
		if (!Files.exists(jsonFilePath)) {
			System.out.println("File does not exist= " + args[0]);
			System.exit(0);
		}

		// 0. Read input from the user
		System.out.println("c Reading input from user");
		SyPetInput jsonInput = JsonParser.parseJsonInput(args[0]);

		// 1. Read configuration file
		System.out.println("c Reading configuration file");
		
		String configPath = "config/config.json";
		SyPetConfig jsonConfig = new SyPetConfig(new ArrayList<>(), new ArrayList<>());		
		Path configFilepath = Paths.get(configPath);

		if (Files.exists(configFilepath))
			jsonConfig = JsonParser.parseJsonConfig(configPath); 
		
		Set<String> acceptableSuperClasses = new HashSet<>();
		acceptableSuperClasses.addAll(jsonConfig.acceptableSuperClasses);

		String methodName = jsonInput.methodName;
		List<String> libs = jsonInput.libs;
		List<String> inputs = jsonInput.srcTypes;
		
		List<String> varNames = jsonInput.paramNames;
		String retType = jsonInput.tgtType;
		File file = new File(jsonInput.testPath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder fileContents = new StringBuilder();
		String line = br.readLine();
		while (line != null) {
			fileContents.append(line);
			line = br.readLine();
		}
		br.close();
		String testCode = fileContents.toString();
		TimerUtils.startTimer("total");
		TimerUtils.startTimer("soot");

		// 2. parse Library
		System.out.println("c Parsing library");
		JarParser parser = new JarParser(libs);
		List<MethodSignature> sigs = parser.parseJar(libs, jsonInput.packages, jsonConfig.blacklist);
		System.out.println("c Creating polymorphic edges");
		Map<String, Set<String>> superclassMap = JarParser.getSuperClasses(acceptableSuperClasses);
		Map<String, Set<String>> subclassMap = new HashMap<>();
		for (String key : superclassMap.keySet()) {
			for (String value : superclassMap.get(key)) {
				if (!subclassMap.containsKey(value)) {
					subclassMap.put(value, new HashSet<String>());
				}
				subclassMap.get(value).add(key);
			}
		}

		TimerUtils.stopTimer("soot");
		// 3. build a petrinet and signatureMap of library
		System.out.println("c Building Petri Net");
		TimerUtils.startTimer("buildnet");
		BuildNet b = new BuildNet();
		PetriNet net = b.build(sigs, superclassMap, subclassMap, inputs, copyPoly);
		Map<String, MethodSignature> signatureMap = b.dict;
		TimerUtils.stopTimer("buildnet");

		int loc_max = 3;
		int loc = 1;
		int paths = 0;
		int programs = 0;
		boolean solution = false;

		while (!solution && loc <= loc_max) {
			TimerUtils.startTimer("path");
			// create a formula that has the same semantics as the petri-net
			Encoding encoding = new SequentialEncoding(net, loc);
			// set initial state and final state
			encoding.setState(EncodingUtil.setInitialState(net, inputs), 0);
			encoding.setState(EncodingUtil.setGoalState(net, retType), loc);

			// 4. Perform reachability analysis

			// for each loc find all possible programs
			List<Variable> result = Encoding.solver.findPath(loc);
			TimerUtils.stopTimer("path");
			while (!result.isEmpty() && !solution) {
				TimerUtils.startTimer("path");
				paths++;
				String path = "Path #" + paths + " =\n";
				List<String> apis = new ArrayList<String>();
				// A list of method signatures
				List<MethodSignature> signatures = new ArrayList<>();
				for (Variable s : result) {
					apis.add(s.getName());
					path += s.toString() + "\n";
					MethodSignature sig = signatureMap.get(s.getName());
					if(sig != null) { //check if s is a line of a code
                        signatures.add(sig);
                    }
				}
				TimerUtils.stopTimer("path");
				if (true) {
					// 5. Convert a path to a program
					boolean sat = true;
					CodeFormer former = new CodeFormer(signatures, inputs, retType, varNames, methodName, subclassMap,
							superclassMap);
					while (sat) {
						TimerUtils.startTimer("code");
						String code;
						try {
							code = former.solve();
						} catch (TimeoutException e) {
							sat = false;
							break;
						}
						sat = !former.isUnsat();
						programs++;
						TimerUtils.stopTimer("code");
						// 6. Run the test cases
						// TODO: write this code; if all test cases pass then we can terminate
						TimerUtils.startTimer("compile");
						boolean compre = Test.runTest(code, testCode, libs);
						TimerUtils.stopTimer("compile");
						if (compre) {
							solution = true;
							writeLog(out, "Options:\n");
							writeLog(out, "Clone: " + clone + "\n");
							writeLog(out, "Copy polymorphism: " + copyPoly + "\n");
							writeLog(out, "Equivalent program: " + equiv + "\n");
							writeLog(out, "Programs explored = " + programs + "\n");
							writeLog(out, "Paths explored = " + paths + "\n");
							writeLog(out, "code:\n");
							writeLog(out, code + "\n");
							writeLog(out, "Soot time: " + TimerUtils.getCumulativeTime("soot") + "\n");
							writeLog(out, "Build graph time: " + TimerUtils.getCumulativeTime("buildnet") + "\n");
							writeLog(out, "Find path time: " + TimerUtils.getCumulativeTime("path") + "\n");
							writeLog(out, "Form code time: " + TimerUtils.getCumulativeTime("code") + "\n");
							writeLog(out, "Compilation time: " + TimerUtils.getCumulativeTime("compile") + "\n");
							break;
						}
					}
				}

				// the current path did not result in a program that passes all test cases
				// find the next path
				result = Encoding.solver.findPath(loc);
			}

			// we did not find a program of length = loc
			loc++;
		}
		out.close();
	}

	private static void writeLog(BufferedWriter out, String string) {
		try {
			out.write(string);
		} catch (IOException e) {
			System.exit(1);
		}
	}

}
