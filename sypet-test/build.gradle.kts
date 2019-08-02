plugins {
    `java-library`
    id("com.diffplug.gradle.spotless")
}

spotless {
    java {
        googleJavaFormat("1.7")
    }
}
