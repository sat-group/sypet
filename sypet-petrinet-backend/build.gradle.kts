plugins {
    `java-library`
    id("com.diffplug.gradle.spotless")
}

dependencies {
    compile(project(":sypet-core"))
}

tasks.register<Copy>("copyJar") {
}

spotless {
    java {
        googleJavaFormat("1.7")
    }
}