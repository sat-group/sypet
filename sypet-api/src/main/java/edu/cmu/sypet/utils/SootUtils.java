/*
 BSD 3-Clause License


 Copyright (c) 2018, SyPet 2.0 - Ruben Martins, Yu Feng, Isil Dillig All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, are permitted
 provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this list of conditions
 and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice, this list of
 conditions and the following disclaimer in the documentation and/or other materials provided with
 the distribution.

 * Neither the name of the copyright holder nor the names of its contributors may be used to
 endorse or promote products derived from this software without specific prior written
 permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package edu.cmu.sypet.utils;

import java.util.Collection;
import soot.CompilationDeathException;
import soot.Scene;
import soot.options.Options;

/** Soot utilities. */
public class SootUtils {

  /**
   * Inits soot classpath and prepares for static analysis.
   *
   * @param libs represents the libraries used for analysis.
   */
  public static void initSoot(Collection<String> libs) {

    final StringBuilder options =
        new StringBuilder()
            .append("-prepend-classpath")
            .append(" -full-resolver")
            .append(" -allow-phantom-refs");

    final StringBuilder classpath = new StringBuilder();

    for (String lib : libs) {
      classpath.append(lib);
      classpath.append(":");
      options.append(" -process-dir ").append(lib);
    }

    if (!libs.isEmpty()) {
      options.append(" -cp ").append(classpath.toString());
    }

    if (!Options.v().parse(options.toString().split(" "))) {
      throw new CompilationDeathException(
          CompilationDeathException.COMPILATION_ABORTED,
          "Option parse error " + options.toString());
    }

    Scene.v().loadNecessaryClasses();
  }
}
