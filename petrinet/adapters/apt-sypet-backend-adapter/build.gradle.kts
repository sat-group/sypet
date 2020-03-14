import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.70"
}

dependencies {
    implementation(fileTree("lib"))
    implementation(project(":sypet-petrinet-backend-api"))
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(project(":sypet-petrinet-test-lib"))
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testImplementation("io.mockk:mockk:1.9")
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