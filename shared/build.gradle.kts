plugins {
    kotlin("js") version "1.4.21"
}

group = "de.mari"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

kotlin {
    js(IR) {
        browser {
            binaries.executable()
        }
    }
}

val node = tasks.register<Copy>("node") {
    copy {
        from("$buildDir/processedResources/js/main")
        include("package.json")
        into("$buildDir/libs/shared-types/")
    }
    copy {
        from("$buildDir/compileSync/kotlin")
        into("$buildDir/libs/shared-types/")
        rename { name -> name.replace("${rootProject.name}-shared", "index") }
    }
}

val jvm = tasks.register<Copy>("jvm") {
    delete("$buildDir/libs/kt-jvm")
    copy {
        from("$projectDir/src/main/kotlin")
        include("**/*.kt")
        into("$buildDir/libs/kt-jvm/")
    }

    ant.withGroovyBuilder {
        "replaceregexp"(
            "match" to "@JsExport",
            "replace" to "// @JsExport - not available nor needed on JVM",
            "flags" to "g"
        ) {
            "fileset"(
                "dir" to "$buildDir/libs/kt-jvm/", "includes" to "**/*.kt"
            )
        }
    }
    ant.withGroovyBuilder {
        "replaceregexp"(
            "match" to "@ExperimentalJsExport",
            "replace" to "// @ExperimentalJsExport - not available nor needed on JVM",
            "flags" to "g"
        ) {
            "fileset"(
                "dir" to "$buildDir/libs/kt-jvm/", "includes" to "**/*.kt"
            )
        }
    }
    // add package to file
    ant.withGroovyBuilder {
        "replaceregexp"(
            "match" to "package ",
            "replace" to "package ${project.group}.types.",
            "flags" to "g"
        ) {
            "fileset"(
                "dir" to "$buildDir/libs/kt-jvm/", "includes" to "**/*.kt"
            )
        }
    }
}

tasks {
    "build" {
        doLast {
            copy {
                from("$buildDir/processedResources/js/main")
                include("package.json")
                into("$buildDir/libs/shared-types/")
            }
            copy {
                from("$buildDir/compileSync/kotlin")
                into("$buildDir/libs/shared-types/")
                rename { name -> name.replace("${rootProject.name}-shared", "index") }
            }

            delete("$buildDir/libs/kt-jvm")

            copy {
                from("$projectDir/src/main/kotlin")
                include("**/*.kt")
                into("$buildDir/libs/kt-jvm/")
            }

            ant.withGroovyBuilder {
                "replaceregexp"(
                    "match" to "@JsExport",
                    "replace" to "// DO NOT EDIT THIS FILE HERE",
                    "flags" to "g"
                ) {
                    "fileset"(
                        "dir" to "$buildDir/libs/kt-jvm/", "includes" to "**/*.kt"
                    )
                }
            }
            ant.withGroovyBuilder {
                "replaceregexp"(
                    "match" to "@ExperimentalJsExport",
                    "replace" to "// DO NOT EDIT THIS FILE HERE",
                    "flags" to "g"
                ) {
                    "fileset"(
                        "dir" to "$buildDir/libs/kt-jvm/", "includes" to "**/*.kt"
                    )
                }
            }
            // add package to file
            ant.withGroovyBuilder {
                "replaceregexp"(
                    "match" to "package ",
                    "replace" to "package ${project.group}.types.",
                    "flags" to "g"
                ) {
                    "fileset"(
                        "dir" to "$buildDir/libs/kt-jvm/", "includes" to "**/*.kt"
                    )
                }
            }
            ant.withGroovyBuilder {
                "replaceregexp"(
                    "match" to "import ",
                    "replace" to "import ${project.group}.types.",
                    "flags" to "g"
                ) {
                    "fileset"(
                        "dir" to "$buildDir/libs/kt-jvm/", "includes" to "**/*.kt"
                    )
                }
            }
        }
        finalizedBy(":client:addPackage")
        finalizedBy(":server:addTypes")
    }
}