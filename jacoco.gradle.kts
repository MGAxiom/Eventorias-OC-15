apply(plugin = "jacoco")

configure<JacocoPluginExtension> {
    toolVersion = "0.8.12"
}

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
    "**/*Module*.*",
    "**/*Dagger*.*",
    "**/*Hilt*.*",
    "**/hilt_aggregated_deps/*",
    "**/*_HiltModules*.*",
    "**/*_Impl*.*",
    "**/*Binding*.*"
)

tasks.register<JacocoReport>("jacocoTestReport") {
    group = "Reporting"
    description = "Generate Jacoco coverage reports"

    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    val buildDir = layout.buildDirectory.get().asFile
    val javaClasses = fileTree(buildDir) {
        include("**/classes/**/main/**")
        exclude(excludedFiles)
    }

    val kotlinClasses = fileTree(buildDir) {
        include("**/tmp/kotlin-classes/debug/**")
        exclude(excludedFiles)
    }

    classDirectories.setFrom(files(javaClasses, kotlinClasses))

    val sourceDirs = files(
        "$projectDir/src/main/java",
        "$projectDir/src/main/kotlin"
    )

    sourceDirectories.setFrom(sourceDirs)

    executionData.setFrom(
        fileTree(buildDir) {
            include("outputs/unit_test_code_coverage/debugUnitTest/*.exec")
            include("jacoco/testDebugUnitTest.exec")
        }
    )
}

tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    group = "Verification"
    description = "Verify code coverage metrics"

    dependsOn("jacocoTestReport")

    violationRules {
        rule {
            limit {
                minimum = "0.60".toBigDecimal()
            }
        }

        rule {
            enabled = true
            element = "CLASS"
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.50".toBigDecimal()
            }
            excludes = listOf(
                "*.BuildConfig",
                "*.di.*",
                "*.*Module",
                "*.*Module\$*"
            )
        }
    }

    val buildDir = layout.buildDirectory.get().asFile
    val javaClasses = fileTree(buildDir) {
        include("**/classes/**/main/**")
        exclude(excludedFiles)
    }

    val kotlinClasses = fileTree(buildDir) {
        include("**/tmp/kotlin-classes/debug/**")
        exclude(excludedFiles)
    }

    classDirectories.setFrom(files(javaClasses, kotlinClasses))
}