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
package edu.cmu.sypet.java;

import static edu.cmu.sypet.java.GsonUtils.getArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** Factory to parse SyPet-specific JSON files. */
public final class JsonParser {

  private JsonParser() {}

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

      // Use a custom deserializer to avoid object construction with null fields.
      final Gson gson =
          new GsonBuilder()
              .registerTypeAdapter(SyPetInput.class, new SyPetInputDeserializer())
              .create();

      return gson.fromJson(reader, SyPetInput.class);
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

      // Use a custom deserializer to avoid object construction with null fields.
      final Gson gson =
          new GsonBuilder()
              .registerTypeAdapter(SyPetInput.class, new SyPetConfigDeserializer())
              .create();

      return gson.fromJson(reader, SyPetConfig.class);
    }
  }
}

/**
 * This class is used to deserialize SyPetInput from JSON.
 *
 * @see SyPetInput
 */
class SyPetInputDeserializer implements JsonDeserializer<SyPetInput> {

  private static final String ID = "id";
  private static final String METHOD_NAME = "methodName";
  private static final String PARAM_NAMES = "paramNames";
  private static final String SRC_TYPES = "srcTypes";
  private static final String TGT_TYPE = "tgtType";
  private static final String PACKAGES = "packages";
  private static final String LIBS = "libs";
  private static final String TEST_PATH = "testPath";
  private static final String LOC_LOWER_BOUND = "locLowerBound";
  private static final String LOC_UPPER_BOUND = "locUpperBound";
  private static final String HINTS = "hints";

  @Override
  public SyPetInput deserialize(JsonElement json, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    final JsonObject jobj = json.getAsJsonObject();
    final Gson gson = new Gson();

    // TODO Rename benchmark files to have same fields as SyPetInput.
    // TODO Use Collection instead of List.
    final int id = jobj.get(ID).getAsInt();
    final String methodName = jobj.get(METHOD_NAME).getAsString();
    final List<String> paramNames =
        getArrayList(gson, jobj.getAsJsonArray(PARAM_NAMES), String[].class);
    final List<String> paramTypes =
        getArrayList(gson, jobj.getAsJsonArray(SRC_TYPES), String[].class);
    final String returnType = jobj.get(TGT_TYPE).getAsString();
    final List<String> packages = getArrayList(gson, jobj.getAsJsonArray(PACKAGES), String[].class);
    final List<String> libs = getArrayList(gson, jobj.getAsJsonArray(LIBS), String[].class);

    final String testPath = jobj.get(TEST_PATH).getAsString();
    final String testCode;
    try {
      testCode = new String(Files.readAllBytes(Paths.get(testPath)));
    } catch (IOException e) {
      throw new JsonParseException(e);
    }

    final SyPetInput.Builder builder =
        new SyPetInput.Builder(
            methodName, paramNames, paramTypes, returnType, packages, libs, testCode);

    if (jobj.has(LOC_LOWER_BOUND)) {
      builder.locLowerBound(jobj.get(LOC_LOWER_BOUND).getAsInt());
    }

    if (jobj.has(LOC_UPPER_BOUND)) {
      builder.locUpperBound(jobj.get(LOC_UPPER_BOUND).getAsInt());
    }

    if (jobj.has(HINTS)) {
      builder.hints(getArrayList(gson, jobj.getAsJsonArray(HINTS), String[].class));
    }

    return builder.build();
  }
}

/**
 * This class is used to deserialize SyPetConfig from JSON.
 *
 * @see SyPetConfig
 */
class SyPetConfigDeserializer implements JsonDeserializer<SyPetConfig> {

  private static final String LOCAL_SUPER_CLASSES = "localSuperClasses";
  private static final String BLACKLIST = "blacklist";
  private static final String NO_SIDE_EFFECTS = "noSideEffects";
  private static final String GLOBAL_SUPER_CLASSES = "globalSuperClasses";

  @Override
  public SyPetConfig deserialize(JsonElement json, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    final JsonObject jobj = json.getAsJsonObject();
    final Gson gson = new Gson();
    final SyPetConfig.Builder configBuilder = new SyPetConfig.Builder();

    if (jobj.has(LOCAL_SUPER_CLASSES)) {
      configBuilder.localSuperClasses(
          getArrayList(gson, jobj.getAsJsonArray(LOCAL_SUPER_CLASSES), String[].class));
    }

    if (jobj.has(BLACKLIST)) {
      configBuilder.blacklist(getArrayList(gson, jobj.getAsJsonArray(BLACKLIST), String[].class));
    }

    if (jobj.has(NO_SIDE_EFFECTS)) {
      configBuilder.noSideEffects(
          getArrayList(gson, jobj.getAsJsonArray(NO_SIDE_EFFECTS), String[].class));
    }

    if (jobj.has(GLOBAL_SUPER_CLASSES)) {
      configBuilder.globalSuperClasses(
          Arrays.stream(gson.fromJson(jobj, String[][].class))
              .map(Arrays::asList)
              .collect(Collectors.toList()));
    }

    return configBuilder.build();
  }
}

class GsonUtils {

  public static <T> ArrayList<T> getArrayList(
      com.google.gson.Gson gson, JsonArray array, Class<T[]> classOfT) {
    return new ArrayList<>(Arrays.asList(gson.fromJson(array, classOfT)));
  }
}
