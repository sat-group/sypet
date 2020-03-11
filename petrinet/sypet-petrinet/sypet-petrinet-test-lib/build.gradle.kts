import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin("jvm") version "1.3.70"
}

dependencies {
    implementation(project(":sypet-petrinet-backend-api"))
    implementation(project(":sypet-petrinet-frontend-api"))
    implementation(project(":sypet-petrinet-lib"))

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("test"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}