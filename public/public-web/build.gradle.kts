import org.springframework.boot.gradle.tasks.bundling.BootJar

description = "SciPaMaTo-Public :: Web Project"

plugins {
    alias(libs.plugins.springBoot).apply(true)
    id("application-properties-filter")
    id("scipamato-integration-test")
}

testing {
    suites {
        @Suppress("unused")
        val integrationTest = named<JvmTestSuite>("integrationTest") {
            // jar is disabled for web projects, so project() alone puts no main classes on the classpath;
            // main goes before the libraries, as in the boot jar, so its application.properties wins
            sources {
                compileClasspath += sourceSets.main.get().output
                runtimeClasspath = output + sourceSets.main.get().output + configurations.named(runtimeClasspathConfigurationName).get()
            }
            dependencies {
                implementation(libs.testcontainers.testcontainers)
                implementation(libs.testcontainers.junitJupiter)
                implementation(libs.testcontainers.postgresql)
                runtimeOnly(libs.postgresql)
            }
        }
    }
}

/**
 * Make the static wicket resources that reside next to the java classes in src{main,test} available.
 */
sourceSets {
    /** main: html, css, sass, properties files */
    main {
        resources {
            setSrcDirs(setOf("src/main/resources", "src/main/kotlin"))
        }
    }
    /** test: html and properties files */
    test {
        resources {
            setSrcDirs(setOf("src/test/resources", "src/test/kotlin"))
        }
    }
}

tasks {
    withType<BootJar> {
        enabled = true
        mainClass.set("ch.difty.scipamato.publ.ScipamatoPublicApplicationKt")
    }
}

dependencies {
    implementation(project(":common-utils"))
    implementation(project(":public-entity"))
    implementation(project(":public-persistence-jooq"))
    implementation(project(":common-wicket"))

    annotationProcessor(libs.spring.boot.configurationprocessor) {
        exclude("om.vaadin.external.google", "android-json")
    }
    implementation(libs.spring.boot.starter.security)

    implementation(libs.postgresql)

    implementation(libs.bundles.caching)
    implementation(libs.ehcache) {
        capabilities {
            requireCapability("org.ehcache:ehcache-jakarta")
        }
    }

    testImplementation(testFixtures(project(":common-wicket")))
    testImplementation(testFixtures(project(":common-persistence-jooq")))
}
