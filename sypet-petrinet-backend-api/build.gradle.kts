plugins {
    `java-library`
    id("com.diffplug.gradle.spotless")
}

tasks.register<Copy>("copyJar") {
}

spotless {
    java {
        googleJavaFormat("1.7")
    }
}