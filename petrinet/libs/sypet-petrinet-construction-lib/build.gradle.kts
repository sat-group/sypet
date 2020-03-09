import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin("jvm") version "1.3.70"
}

dependencies {
    implementation("io.github.kostaskougios:cloning:1.10.1")

    testImplementation(kotlin("stdlib-jdk8"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.5.1")
    testImplementation(kotlin("test"))

    testImplementation(project(":apt-sypet-backend-adapter"))
}

tasks.test {
    useJUnitPlatform()
    ignoreFailures = true
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}