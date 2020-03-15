data class Dir(val path: String)

fun root(name: String, include: () -> Unit) {
    rootProject.name = name
    include()
}

fun dir(name: String, include: Dir.() -> Unit) {
    Dir(path = name).include()
}

fun Dir.dir(name: String, include: Dir.() -> Unit) {
    Dir(path = "$path/$name").include()
}

fun Dir.module(name: String) {
    include(name)
    project(":$name").projectDir = file("$path/$name")
}

root(name = "sypet") {
    dir("petrinet") {
        dir("adapters") {
            module("apt-sypet-backend-adapter")
        }
        dir("sypet-petrinet") {
            module("sypet-petrinet-backend-api")
            module("sypet-petrinet-frontend-api")
            module("sypet-petrinet-lib")
            module("sypet-petrinet-test-lib")
        }
        dir("sypet-petrinet-reachability") {
            module("sypet-reachability-lib")
        }
    }
    dir("sypet") {
        dir("sypet-synthesiser") {
            module("sypet-synthesiser-backend-api")
            module("sypet-synthesiser-frontend-api")
            module("sypet-synthesiser-lib")
        }
    }
}