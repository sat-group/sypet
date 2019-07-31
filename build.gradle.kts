subprojects {
    version = "2.0"
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
    }
}

configure(subprojects) {
    apply(plugin = "java")
}
