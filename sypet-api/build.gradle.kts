plugins {
    `java-library`
}

dependencies {
    // sat4j must come before apt on the classpath, because apt comes with another version of sat4j.
    implementation(files("lib/sat4j-pb.jar"))

    implementation("com.google.code.gson:gson:2.8.5")
    implementation("ca.mcgill.sable:soot:3.3.0")
    implementation("org.apache.commons:commons-lang3:3.9")

    implementation(fileTree("lib"))

    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.1")

    testImplementation(project(":sypet-test"))
}

tasks.test {
    dependsOn("copyJar")
    useJUnitPlatform()
}

tasks.register<Copy>("copyJar") {
    from(file("../sypet-test/build/libs/sypet-test-$version.jar"))
    into(file("src/test/resources"))
    rename("(sypet-test)-$version(\\.jar)", "$1$2")
}
