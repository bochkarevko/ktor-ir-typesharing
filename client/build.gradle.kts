import org.siouan.frontendgradleplugin.infrastructure.gradle.RunYarn

plugins {
    id("org.siouan.frontend-jdk17") version "7.0.0"
}

frontend {
    nodeDistributionProvided.set(false)
    nodeVersion.set("18.16.0")
    nodeDistributionUrlRoot.set("https://nodejs.org/dist/")
    nodeDistributionUrlPathPattern.set("vVERSION/node-vVERSION-ARCH.TYPE")
    nodeInstallDirectory.set(project.layout.projectDirectory.dir("node"))

    installScript.set("install")
    assembleScript.set("build")
}

val removeSharedTypesTask = tasks.register<RunYarn>("removeSharedTypes") {
    script = "remove shared-types"
}

tasks.register<RunYarn>("addSharedTypes") {
    dependsOn(removeSharedTypesTask)
    script = "add file:../shared/build/libs/shared-types/"
}

tasks.get("build").dependsOn(":shared:build")

tasks.get("build").doLast {
    copy {
        from("$projectDir/dist/")
        into("../server/resources/dist/")
    }
}