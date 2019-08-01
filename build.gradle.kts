plugins {
    id("com.github.johnrengelman.shadow") version "5.1.0" apply false
}

subprojects {
    version = "2.0"
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
    }
}
