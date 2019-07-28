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

import cmu.edu.parser.JsonParser;
import cmu.edu.parser.SyPetConfig;
import cmu.edu.parser.SyPetInput;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * This class represents the main entry point to the SyPet synthesis tool.
 */
public final class SyPet {

  private SyPet() {
  }

  // TODO Explain the JSON format in more detail.

  /**
   * Starts the synthesis loop. It accepts the path to a JSON file with configuration options and
   * information about the program we want to synthesize.
   *
   * @param args a single element array whose element is the path string to the JSON file
   * @throws IOException if it fails to read the contents of the Java test case file.
   */
  public static void main(String[] args) {
    // We expect a single string argument representing the path to the JSON file.
    if (args.length != 1) {
      System.err.println("Error: wrong number of arguments: " + args.length);
      System.err.println("Usage: ./sypet.sh <filename.json>");
      System.exit(1);
    }

    final Path jsonInputFile = Paths.get(args[0]);

    if (!Files.exists(jsonInputFile)) {
      System.err.println("File not found: " + jsonInputFile.toString());
      System.exit(1);
    }

    // TODO This should not be hardcoded.
    final Path jsonConfigFile = Paths.get("./config/config.json");

    if (!Files.exists(jsonInputFile)) {
      System.err.println("Config file not be found: " + jsonConfigFile.toString());
      System.exit(1);
    }

    final Optional<String> program = SyPet.synthesize(jsonInputFile, jsonConfigFile);

    if (program.isPresent()) {
      System.out.println("c Synthesized program:\n" + program.get());
      System.exit(0);
    } else {
      System.out.println("c Failed to synthesize program.");
      System.exit(1);
    }
  }

  /**
   * Synthesize a program from a <code>SyPetInput</code> input object and a <code>SyPetConfig</code>
   * configuration object.
   *
   * @param input the input object
   * @param config the configuration object
   * @return optionally a program, if one can be synthesized
   * @see SyPetInput
   */
  protected static Optional<String> synthesize(SyPetInput input, SyPetConfig config) {
    UISyPet sypet = new UISyPet(input, config);
    sypet.setSignature(input.methodName, input.paramNames, input.srcTypes, input.tgtType,
        input.testBody);
    return sypet.synthesize(input.lb, input.ub);
  }

  /**
   * Synthesize program from a JSON file with information about the program we want to synthesize,
   * and from a JSON file with configuration options for SyPet.
   *
   * @param jsonInputFile the path to the JSON file
   * @return optionally a program, if one can be synthesized
   */
  protected static Optional<String> synthesize(Path jsonInputFile, Path jsonConfigFile) {
    try {
      final SyPetInput input = JsonParser.parseJsonInput(jsonInputFile);
      final SyPetConfig config = JsonParser.parseJsonConfig(jsonConfigFile);
      return SyPet.synthesize(input, config);
    } catch (IOException e) {
      System.err.println("Error while trying to read from " + jsonInputFile);
      e.printStackTrace();
      System.exit(1);
    }

    System.err.println("Error: this line should not be reachable");
    System.exit(1);

    return null;
  }

}
