/*
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
import java.util.List;

import cmu.edu.parser.JsonParser;
import cmu.edu.parser.SyPetInput;

/**
 * This class represents the main entry point to the SyPet synthesis tool.
 * 
 * @author Ruben Martins
 * @author rodamber
 */
public class SyPet {

    // TODO Explain the JSON format in more detail.

	/**
	 * Starts the synthesis loop. It accepts the path to a JSON file with configuration options
	 * and information about the program we want to synthesize.
	 *
	 * @param args a single element array whose element is the path string to the JSON file
	 * @throws IOException
	 *         if it fails to read the contents of the Java test case file.
	 */
	public static void main(String[] args) throws IOException {
		// TODO Write here a small overview of how this methods works. The code should then be
		//  simple to understand with minimal comments.

		// We expect a single string argument representing the path to the JSON file.
		if (args.length != 1) {
			System.out.println("Error: wrong number of arguments= " + args.length);
			System.out.println("Usage: ./sypet.sh <filename.json>");

			// TODO Are we terminating successfully?
			System.exit(0);
		}

		// TODO Extract args[0] to a variable, or just use this path variable (or both).
		final Path jsonFilePath = Paths.get(args[0]);

		if (!Files.exists(jsonFilePath)) {
			System.out.println("File does not exist= " + args[0]);

			// TODO Are we terminating successfully?
			System.exit(0);
		}

		final SyPetInput jsonInput    = JsonParser.parseJsonInput(args[0]);
		final Path       testCasePath = Paths.get(jsonInput.testPath);

		// TODO This could probably be moved up next to the rest of checks.
		if (!Files.exists(testCasePath)) {
			System.out.println("File does not exist= " + args[0]);

			// TODO Are we terminating successfully?
			System.exit(0);
		}

		// TODO Clean this mess.
		// Read the contents of the Java test file.
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

		// TODO Use optional instead.
		final List<String> hints = jsonInput.hints != null ? jsonInput.hints : new ArrayList<>();

		// TODO Consider passing jsonInput as a parameter to UISyPet instead.
		final UISyPet sypet = new UISyPet(jsonInput.packages, jsonInput.libs, hints);

		// TODO Why is this done here and not at object construction time?
		sypet.setSignature(jsonInput.methodName, jsonInput.paramNames, jsonInput.srcTypes,
				jsonInput.tgtType, testCode);

		final String program = sypet.synthesize(jsonInput.lb, jsonInput.ub);
		// TODO Weird condition. This is probably a test for failure. Check if we can refactor this.
		if (!program.equals("")) {
			System.out.println("c Synthesized code:\n" + program);
		}

		System.exit(0);
	}

}
