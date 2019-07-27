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
package cmu.edu.parser;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

// TODO There's no real need for this class. These methods could be made constructors of their
//  correspondent return types.

/**
 * Factory to parse SyPet-specific JSON files.
 */
public final class JsonParser {

  private JsonParser() {
  }

  /**
   * Parse input to SyPet from a JSON file.
   *
   * @param file the file path to the JSON file
   * @return the input object
   * @throws NullPointerException If the <code>path</code> is <code>null</code>
   * @throws IOException If an I/O error occurs while trying to read <code>file</code>
   * @see SyPetInput
   */
  public static SyPetInput parseJsonInput(Path file) throws IOException {
    Objects.requireNonNull(file);

    try (InputStream in = Files.newInputStream(file)) {
      final Reader reader = new InputStreamReader(in);
      return new Gson().fromJson(reader, SyPetInput.class);

      // TODO Add custom deserializer to avoid object construction with nulls.
//			final Gson gson = new GsonBuilder()
//					.registerTypeAdapter(SyPetInput.class, new SyPetInputDeserializer())
//					.create();
//
//			return gson.fromJson(reader, SyPetInput.class);
    }
  }

  /**
   * Parse SyPet configuration settings from a JSON file.
   *
   * @param file the path to the configuration file
   * @return the input configuration
   * @throws NullPointerException If the <code>path</code> is <code>null</code>
   * @throws IOException If an I/O error occurs while trying to read <code>file</code>
   */
  public static SyPetConfig parseJsonConfig(Path file) throws IOException {
    Objects.requireNonNull(file);

    try (InputStream in = Files.newInputStream(file)) {
      final Reader reader = new InputStreamReader(in);
      return new Gson().fromJson(reader, SyPetConfig.class);
    }
  }
}
