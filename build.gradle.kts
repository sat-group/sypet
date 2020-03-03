plugins {
    id("com.github.johnrengelman.shadow") version "5.1.0" apply false
    id("com.diffplug.gradle.spotless") version "3.24.0"
}

subprojects {
    version = "2.0"
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
    }

    tasks.withType<JavaCompile>().configureEach {
        options.isDeprecation = true
        options.compilerArgs.add("-Xlint:unchecked")
    }
}

spotless {
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}
