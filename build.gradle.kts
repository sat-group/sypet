plugins {
    id("com.diffplug.gradle.spotless") version "3.24.0" apply true
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
    }
}

subprojects {
    version = "2.0"

    if (plugins.hasPlugin("java")) {
        tasks.withType<JavaCompile>().configureEach {
            options.isDeprecation = true
            options.compilerArgs.add("-Xlint:unchecked")
        }

        spotless {
            java {
                googleJavaFormat("1.7")
            }
        }
    }
}

spotless {
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}
