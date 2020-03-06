import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    id("com.diffplug.gradle.spotless")
    kotlin("jvm") version "1.3.70"
}

dependencies {
    compile(project(":reachability-lib"))

    // sat4j must come before apt on the classpath, because apt comes with another version of sat4j.
    implementation(fileTree("../reachability-lib/lib/sat4j-pb.jar"))
    implementation(fileTree("lib"))

    implementation("org.apache.commons:commons-lang3:3.9")
    implementation("com.google.guava:guava:28.0-jre")
    implementation("io.github.classgraph:classgraph:4.8.43")
    implementation("io.github.kostaskougios:cloning:1.10.1")

    compileOnly("org.immutables:value-annotations:2.7.1")
    annotationProcessor("org.immutables:value:2.7.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.5.1")
    testImplementation(project(":sypet-test"))
    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.9.3")
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

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}