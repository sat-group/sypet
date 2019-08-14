plugins {
    `java-library`
    id("com.diffplug.gradle.spotless")
}

dependencies {
    implementation(fileTree("lib"))
    implementation("org.apache.commons:commons-lang3:3.9")
}

spotless {
    java {
        googleJavaFormat("1.7")
    }
}
