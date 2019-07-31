plugins {
    application
}

dependencies {
    compile(project(":sypet-api"))
}

application {
    mainClassName = "edu.cmu.sypet.SyPetCLI"
}
