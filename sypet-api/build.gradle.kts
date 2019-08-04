plugins {
    `java-library`
    id("com.diffplug.gradle.spotless")
}

dependencies {
    // sat4j must come before apt on the classpath, because apt comes with another version of sat4j.
    implementation(files("lib/sat4j-pb.jar"))
    implementation(fileTree("lib"))

    implementation("ca.mcgill.sable:soot:3.3.0")
    implementation("org.apache.commons:commons-lang3:3.9")
    implementation("com.google.guava:guava:28.0-jre")

    compileOnly("org.immutables:value-annotations:2.7.1")
    annotationProcessor("org.immutables:value:2.7.1")

    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.1")

    testImplementation(project(":sypet-test"))
}

tasks.test {
    dependsOn("copyJar")
    useJUnitPlatform()
    ignoreFailures = true
}

tasks.register<Copy>("copyJar") {
    from(file("../sypet-test/build/libs/sypet-test-$version.jar"))
    into(file("src/test/resources"))
    rename("(sypet-test)-$version(\\.jar)", "$1$2")
}

spotless {
    java {
        googleJavaFormat("1.7")
    }
}
