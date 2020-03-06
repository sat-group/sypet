plugins {
    `java-library`
    id("com.diffplug.gradle.spotless")
}

tasks.register<Copy>("copyJar") {
    from(file("../sypet-test/build/libs/sypet-test-$version.jar"))
    into(file("src/test/resources"))
    rename("(sypet-test)-$version(\\.jar)", "$1$2")
}

spotless {
    java {
        googleJavaFormat("1.7")
    }
}
