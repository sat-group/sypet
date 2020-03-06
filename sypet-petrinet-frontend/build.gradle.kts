import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    id("com.diffplug.gradle.spotless")
    kotlin("jvm") version "1.3.70"
}

dependencies {
    compile(project(":sypet-core"))

    implementation("io.github.kostaskougios:cloning:1.10.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.5.1")
    testImplementation(project(":sypet-petrinet-backend"))
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