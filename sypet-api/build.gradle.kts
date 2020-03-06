plugins {
    `java-library`
    id("com.diffplug.gradle.spotless")
}

dependencies {
    compile(project(":sypet-lang-api"))
}

tasks.register<Copy>("copyJar") {
}

spotless {
    java {
        googleJavaFormat("1.7")
    }
}
