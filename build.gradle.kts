// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.12"
}

val androidSubprojects = subprojects.filter {
    it.name in listOf("app", "core-kt", "core-ui", "data", "domain")
}

val excludes = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/*Test*.*",
    "android/**/*.*",
    "**/Manifest*.*",
    "**/*\$ViewInjector*.*",
    "**/*\$ViewBinder*.*",
    "**/Lambda\$*.class",
    "**/Lambda.class",
    "**/*Lambda.class",
    "**/*Lambda*.class",
    "**/*_MembersInjector.class",
    "**/Dagger*Component*.*",
    "**/*Module_*Factory.class",
    "**/di/*",
    "**/*_Factory*.*",
    "**/*Module*.*",
    "**/*Dagger*.*",
    "**/*Hilt*.*",
    "**/hilt_aggregated_deps/*",
    "**/*_HiltModules*.*",
    "**/*_Impl*.*",
    "**/*Binding*.*"
)

tasks.register<org.gradle.testing.jacoco.tasks.JacocoReport>("jacocoTestReportAll") {
    doNotTrackState("Workaround for JaCoCo and Gradle state tracking issue")

    dependsOn(androidSubprojects.map { project ->
        project.tasks.named("testDebugUnitTest")
    })

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    val sourceDirs = androidSubprojects.flatMap { project ->
        listOf(
            project.file("src/main/java"),
            project.file("src/main/kotlin")
        )
    }
    sourceDirectories.setFrom(files(sourceDirs))

    val classDirs = files(androidSubprojects.map { project ->
        project.fileTree("build/tmp/kotlin-classes/debug") {
            exclude(excludes)
        }
    })
    classDirectories.setFrom(classDirs)


    val executionDataFiles = files(androidSubprojects.map { project ->
        project.fileTree("build") {
            include("outputs/unit_test_code_coverage/debugUnitTest/*.exec")
        }
    })
    executionData.setFrom(executionDataFiles)
}

tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerificationAll") {
    dependsOn("jacocoTestReportAll")

    val sourceDirs = androidSubprojects.flatMap { project ->
        listOf(
            project.file("src/main/java"),
            project.file("src/main/kotlin")
        )
    }
    sourceDirectories.setFrom(files(sourceDirs))

    val classDirs = files(androidSubprojects.map { project ->
        project.fileTree("build/tmp/kotlin-classes/debug") {
            exclude(excludes)
        }
    })
    classDirectories.setFrom(classDirs)

    val executionDataFiles = files(androidSubprojects.map { project ->
        project.fileTree("build") {
            include("outputs/unit_test_code_coverage/debugUnitTest/*.exec")
        }
    })
    executionData.setFrom(executionDataFiles)

    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}
