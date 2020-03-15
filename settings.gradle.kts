data class Module(val parent: String)

fun root(name: String, include: () -> Unit) {
    rootProject.name = name
    include()
}

fun _module(name: String, include: Module.() -> Unit = {}) {
    include(":$name")
    Module(parent = name).include()
}

fun module(name: String, include: Module.() -> Unit = {}) {
    _module(name, include)
}

fun Module.module(name: String, include: Module.() -> Unit = {}) {
    _module("$parent:$name", include)
}

root(name = "sypet") {
    module("sypet-apps")
    module("sypet-domain")
    module("sypet-core")
    module("sypet-primary-ports")
    module("sypet-reachability")
    module("sypet-secondary-ports")
}