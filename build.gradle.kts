plugins {
    java
    application
    alias(libs.plugins.style)
    alias(libs.plugins.jagr.gradle)
    alias(libs.plugins.javafx)
}

version = file("version").readLines().first()

javafx {
    version = "17.0.1"
    modules("javafx.controls", "javafx.fxml", "javafx.swing")
}

jagr {
    assignmentId.set("p3")
    submissions {
        val main by creating {
            studentId.set("ab12cdef")
            firstName.set("sol_first")
            lastName.set("sol_last")
        }
    }
    graders {
        val graderPublic by creating {
            graderName.set("AuD-2023-P3-Public")
            rubricProviderName.set("p3.P3_RubricProvider")
            configureDependencies {
                implementation(libs.algoutils.tutor)
                implementation(libs.junit.pioneer)
            }
        }
        val graderPrivate by creating {
            parent(graderPublic)
            graderName.set("AuD-2023-P3-Private")
        }
    }
}

dependencies {
    implementation(libs.annotations)
    implementation(libs.algoutils.student)
    testImplementation(libs.junit.core)
}

application {
    mainClass.set("p3.Main")
}

tasks {
    val runDir = File("build/run")
    withType<JavaExec> {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
    }
    test {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
        useJUnitPlatform()
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}
