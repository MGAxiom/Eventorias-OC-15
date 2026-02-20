// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

apply(plugin = "jacoco")

tasks.register<JacocoReport>("jacocoTestReportAll") {
    group = "Reporting"
    description = "Generate Jacoco coverage reports for all modules"

    val excludedFiles = listOf(
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
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
        "**/*Module*.*"
    )

    // Depend on all test tasks from subprojects
    subprojects.forEach { project ->
        val testTask = project.tasks.findByName("testDebugUnitTest")
            ?: project.tasks.findByName("test")
        testTask?.let { dependsOn(it) }
    }

    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("${layout.buildDirectory.get()}/reports/jacoco/jacocoTestReportAll/jacocoTestReportAll.xml"))

        html.required.set(true)
        html.outputLocation.set(file("${layout.buildDirectory.get()}/reports/jacoco/jacocoTestReportAll/html"))

        csv.required.set(false)
    }

    // Collect source directories from all subprojects
    sourceDirectories.setFrom(
        files(subprojects.flatMap { project ->
            listOf(
                "${project.projectDir}/src/main/java",
                "${project.projectDir}/src/main/kotlin"
            )
        })
    )

    // Collect class files from all subprojects
    classDirectories.setFrom(
        files(subprojects.flatMap { project ->
            val buildDir = project.layout.buildDirectory.get().asFile
            listOf(
                fileTree("$buildDir/tmp/kotlin-classes/debug") {
                    exclude(excludedFiles)
                },
                fileTree("$buildDir/intermediates/javac/debug") {
                    exclude(excludedFiles)
                },
                fileTree("$buildDir/classes/kotlin/main") {
                    exclude(excludedFiles)
                }
            )
        })
    )

    // Collect execution data from all subprojects
    executionData.setFrom(
        files(subprojects.flatMap { project ->
            val buildDir = project.layout.buildDirectory.get().asFile
            fileTree(buildDir) {
                include("outputs/unit_test_code_coverage/debugUnitTest/*.exec")
                include("jacoco/*.exec")
            }
        })
    )
}