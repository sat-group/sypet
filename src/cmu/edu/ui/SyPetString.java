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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cmu.edu.parser.JsonParser;
import cmu.edu.parser.SyPetInput;
import cmu.edu.parser.SyPetInputString;

/**
 * This represents the main entry point to synthesis loop from SyPet.
 * 
 * @author Ruben Martins
 */
public class SyPetString {
	
    static HashMap<String, String> constantsString = new HashMap<>(); 
    static HashMap<Integer, String> constantsInteger = new HashMap<>();
    
    public static void initConstantMaps() {
    	constantsInteger.put(0, "get0");
    	constantsInteger.put(1, "get1");
    	constantsInteger.put(2, "get2");
    	constantsInteger.put(3, "get3");
    	constantsInteger.put(4, "get4");
    	constantsInteger.put(5, "get5");
    	constantsInteger.put(6, "get6");
    	constantsInteger.put(7, "get7");
    	constantsInteger.put(8, "get8");
    	constantsInteger.put(9, "get8");
    	constantsInteger.put(10, "get10");
    	constantsInteger.put(20, "get20");
    	constantsInteger.put(30, "get30");
    	constantsInteger.put(40, "get40");
    	constantsInteger.put(50, "get50");
    	constantsInteger.put(60, "get60");
    	constantsInteger.put(70, "get70");
    	constantsInteger.put(80, "get80");
    	constantsInteger.put(90, "get90");
    	constantsInteger.put(100, "get100");
    	constantsInteger.put(15, "get15");
    	constantsInteger.put(16, "get16");
    	
    	constantsString.put("", "getEmpty");
    	constantsString.put("...", "getDots");
    	constantsString.put("/", "getFowardslash");
    	constantsString.put("\\", "getBackwardslash");
    	constantsString.put(" ", "getSpace");
    	constantsString.put("-", "getDash");
    	constantsString.put(".", "getPeriod");
    	constantsString.put("@", "getArroba");
    	constantsString.put(",", "getComma");
    	constantsString.put("_", "getUnderscore");
    	constantsString.put("#", "getPound");
    	constantsString.put("(", "getLeftParenthesis");
    	constantsString.put(")", "getRightParenthesis");
    	constantsString.put("|", "getVert");
    	constantsString.put("<", "getLeftAngle");
    	constantsString.put(">", "getRightAngle");
    	constantsString.put(":", "getColon");
    	constantsString.put("Created on", "getCreatedOne");
    	constantsString.put("update on", "getUpdatedOn");
    	constantsString.put("\\\\", "getDoubleBackwardslash");
    	constantsString.put(" At:", "getAt");
    	
    }
    

	public static void main(String[] args) throws IOException {
		
		initConstantMaps();

		if (args.length > 2) {
			System.out.println("Error: wrong number of arguments= " + args.length);
			System.out.println("Usage: ./sypet.sh <filename.json>");
			System.exit(0);
		}

		Path jsonFilePath = Paths.get(args[0]);
		if (!Files.exists(jsonFilePath)) {
			System.out.println("File does not exist= " + args[0]);
			System.exit(0);
		}

		SyPetInputString jsonInput = JsonParser.parseJsonStringInput(args[0]);
		boolean constants = false;
		if (args.length == 2) constants = true;		
		
		int nb_param = jsonInput.examples.get(0).inputs.size();
			
		List<String> packages = new ArrayList<>();
		packages.add("cmu.edu");
		if (!constants) packages.add("constants.cmu.edu");
		else packages.add("constants.extended.cmu.edu");
		List<String> libs = new ArrayList<String>(Arrays.asList("./lib/systring.jar"));
		List<String> hints = new ArrayList<>();
		
		List<String> blacklist = new ArrayList<>();
		List<String> whitelist = new ArrayList<>();
		
		for (String s : jsonInput.strconst) {
			assert (constantsString.containsKey(s));
			whitelist.add(constantsString.get(s));
		}
		
		for (Integer s : jsonInput.intconst) {
			assert (constantsInteger.containsKey(s));
			whitelist.add(constantsInteger.get(s));
		}
		
		Iterator entries = constantsString.entrySet().iterator();
		while (entries.hasNext()) {
		    Map.Entry entry = (Map.Entry) entries.next();
		    String value = (String)entry.getValue();
		    blacklist.add(value);
		}
		Iterator entries2 = constantsInteger.entrySet().iterator();
		while (entries2.hasNext()) {
		    Map.Entry entry = (Map.Entry) entries2.next();
		    String value = (String)entry.getValue();
		    blacklist.add(value);
		}
		blacklist.removeAll(whitelist);
		if (!constants) blacklist.clear();
//		System.out.println("blacklist = " + blacklist);
//		System.out.println("packages = " + packages);
		UISyPet sypet = new UISyPet(packages, libs, hints, blacklist, true);
		String methodName = "synth";
		List<String> paramNames = new ArrayList<>();
		for (int i = 0; i < nb_param; i++) {
			paramNames.add("sypet_arg" + i);
		}
		List<String> srcTypes = new ArrayList<>();
		for (int i = 0; i < nb_param; i++) {
			if (jsonInput.examples.get(0).inputs.get(i).Left == null)
				srcTypes.add("int");
			else
				srcTypes.add("java.lang.String");
		}
		// all our examples have output string
		String tgtType = "java.lang.String";
		
		// create test case
		String testCode = "public boolean test() throws Throwable {\n";
		assert (nb_param <= 3);
		for (int i = 0; i < jsonInput.examples.size(); i++) {
			for (int j = 0; j < nb_param; j++) {
				if (jsonInput.examples.get(i).inputs.get(j).Left == null)
					testCode += "int i" + i + "_"+j+" = " + jsonInput.examples.get(i).inputs.get(j).Right + ";\n";
				else
					testCode += "java.lang.String i" + i + "_"+j+ " = \"" + jsonInput.examples.get(i).inputs.get(j).Left + "\";\n"; 
			}
			testCode += "java.lang.String o" + i + " = \"" + jsonInput.examples.get(i).output.Left + "\";\n";
			if (nb_param == 1)
				testCode += "java.lang.String r"+i+ " = synth(i"+i+"_"+0+");\n";
			else if(nb_param == 2)
				testCode += "java.lang.String r"+i+ " = synth(i"+i+"_"+0+",i"+i+"_"+1+");\n";
			else 
				testCode += "java.lang.String r"+i+ " = synth(i"+i+"_"+0+",i"+i+"_"+1+",i"+i+"_"+2+");\n";
		}
		// FIXME: hardcoded for 3 examples
		testCode += "return (r0.equals(o0) && r1.equals(o1) && r2.equals(o2));\n";
		testCode += "}\n";
		
		System.out.println("Test = " + testCode);
//		System.out.println("methodName = " + methodName);
//		System.out.println("paramNames = " + paramNames);
//		System.out.println("src = " + srcTypes);
//		System.out.println("tgt = " + tgtType);
		
		sypet.setSignature(methodName, paramNames, srcTypes, tgtType, testCode);
		String code = sypet.synthesize(jsonInput.lb, jsonInput.ub);
		if (!code.equals(""))
			System.out.println("c Synthesized code:\n" + code);
		System.exit(0);
	}

}
