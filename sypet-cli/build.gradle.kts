plugins {
    java
    application
    id("com.github.johnrengelman.shadow")
}

dependencies {
    compile(project(":sypet-api"))
}

application {
    mainClassName = "edu.cmu.sypet.SyPetCLI"
}

shadow {
    applicationDistribution
}
