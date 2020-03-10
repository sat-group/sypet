/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 *
 * Detailed information about configuring a multi-project build in Gradle can be found
 * in the user manual at https://docs.gradle.org/5.5.1/userguide/multi_project_builds.html
 */

rootProject.name = "sypet"

// Declare the projects under the folder `sypet`.

val `sypet-adapters` = listOf<String>().map { "$it-adapter" }

val `sypet-apis` = listOf(
// "compiler",
"lang",
// "runtime",
// "sketch-solver",
// "sketcher",
"synthesiser"
).map { "$it-api" }

val `sypet-apps` = listOf<String>(
// "cli",
// "gui",
// "intellij-plugin",
// "rest-api"
)

val `sypet-libs` = listOf<String>(
// "compiler",
// "java-lang",
// "runtime",
// "sketch-solver",
// "sketcher",
// "synthesiser"
).map { "$it-lib" }

val sypet = listOf(
    `sypet-adapters`,
    `sypet-apis`,
    `sypet-apps`,
    `sypet-libs`
).flatten()

// Declare the projects under the folder `petrinet`.

val `petrinet-adapters` = listOf(
    "apt-sypet-backend"
).map { "$it-adapter" }

val `petrinet-apis` = listOf(
"sypet-petrinet-backend",
"sypet-petrinet-frontend"
).map { "$it-api" }

val `petrinet-libs` = listOf(
    "sypet-petrinet",
    "sypet-petrinet-test",
"sypet-petrinet-reachability"
).map { "$it-lib" }

val petrinet = listOf(
    `petrinet-adapters`,
    `petrinet-apis`,
    `petrinet-libs`
).flatten()

val includes = listOf(
    sypet,
    petrinet
).flatten()

// Include all the projects.
includes.map { include(it) }

// Tell Gradle where to find the projects.

fun String.setProjectDir(dir: String) {
    project(":$this").projectDir = file("$dir/$this")
}

fun List<String>.setProjectDir(dir: String) {
    map { it.setProjectDir(dir) }
}

val `sypet-dir` = "sypet"

`sypet-adapters`.setProjectDir("$`sypet-dir`/adapters")
`sypet-apis`.setProjectDir("$`sypet-dir`/apis")
`sypet-apps`.setProjectDir("$`sypet-dir`/apps")
`sypet-libs`.setProjectDir("$`sypet-dir`/libs")

val `petri-net-dir` = "petrinet"

`petrinet-adapters`.setProjectDir("$`petri-net-dir`/adapters")
`petrinet-apis`.setProjectDir("$`petri-net-dir`/apis")
`petrinet-libs`.setProjectDir("$`petri-net-dir`/libs")