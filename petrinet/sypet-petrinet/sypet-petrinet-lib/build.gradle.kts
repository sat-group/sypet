plugins {
    `java-library`
}

dependencies {
    implementation(project(":sypet-petrinet-backend-api"))
    implementation(project(":sypet-petrinet-frontend-api"))
}

tasks.test {
    useJUnitPlatform()
    ignoreFailures = true
}