version = "2.0"

plugins {
    java
    application
}

repositories {
    jcenter()
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    // sat4j must come before apt on the classpath, because apt comes with another version of sat4j.
    implementation(files("lib/sat4j-pb.jar"))

    implementation("com.google.code.gson:gson:2.8.5")
    implementation("ca.mcgill.sable:soot:3.3.0")
    implementation("org.apache.commons:commons-lang3:3.9")

    implementation(fileTree("lib"))

    // Use JUnit Jupiter API for testing.
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")

    // Use JUnit Jupiter Engine for testing.
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

application {
    mainClassName = "edu.cmu.sypet.SyPetCLI"
}

val test by tasks.getting(Test::class) {
    // Use junit platform for unit tests
    useJUnitPlatform()
}
