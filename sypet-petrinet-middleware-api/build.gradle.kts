plugins {
    `java-library`
    id("com.diffplug.gradle.spotless")
}

dependencies {
    compile(project(":sypet-lang-api"))
    compile(project(":sypet-petrinet-backend-api"))
}

tasks.register<Copy>("copyJar") {
}

spotless {
    java {
        googleJavaFormat("1.7")
    }
}