plugins {
    java
    application
    id("com.github.johnrengelman.shadow")
}

dependencies {
    compile(project(":sypet-api"))
    implementation("com.google.code.gson:gson:2.8.5")
}

application {
    mainClassName = "edu.cmu.sypet.SyPetCLI"
}

shadow {
    applicationDistribution
}
