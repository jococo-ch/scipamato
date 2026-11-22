import org.springframework.boot.gradle.tasks.bundling.BootJar

description = "SciPaMaTo-Core :: Web GUI Project"

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
            srcDir("src/main/java")
        }
    }
    /** test: html and properties files */
    test {
        resources {
            srcDir("src/test/kotlin")
        }
    }
}

tasks {
    withType<BootJar> {
        enabled = true
        mainClass.set("ch.difty.scipamato.core.ScipamatoCoreApplicationKt")
    }
}

dependencies {
    implementation(project(":common-utils"))
    implementation(project(":core-entity"))
    implementation(project(":core-logic"))
    implementation(project(":core-persistence-jooq"))
    implementation(project(":core-pubmed-api"))
    implementation(project(":core-sync"))
    implementation(project(":common-wicket"))
    implementation(libs.spring.boot.starter.security)
    implementation(libs.coroutines.core)
    implementation(libs.wicketstuff.jasperreports) {
        exclude("net.sf.jasperreports", "jasperreports")
        exclude("commons-logging", "commons-logging")
    }
    implementation(libs.jasperreports) {
        exclude("commons-lang", "commons-lang")
        exclude("commons-collections", "commons-collections")
        exclude("commons-logging", "commons-logging")
    }
    implementation(libs.jasperreports.jdt)
    implementation(libs.jasperreports.fonts)
    implementation(libs.jasperreports.pdf)
    implementation(libs.univocity)
    // temporarily needed until guava has been fully removed from wicket-bootstrap
    implementation("com.google.guava:guava:33.7.2-jre")
    implementation(libs.kris.core)
    implementation(libs.postgresql)
    /** Caching: JCache with ehcache as cache provider */
    implementation(libs.bundles.caching)
    implementation(libs.ehcache) {
        capabilities {
            requireCapability("org.ehcache:ehcache-jakarta")
        }
    }

    annotationProcessor(libs.spring.boot.configurationprocessor) {
        exclude("om.vaadin.external.google", "android-json")
    }

    testImplementation(testFixtures(project(":common-wicket")))
    testImplementation(testFixtures(project(":common-persistence-jooq")))
    testImplementation(libs.jakarta.servletApi)
    testImplementation(libs.validationApi)
    testImplementation(libs.lombok)

    testAnnotationProcessor(libs.lombok)
}

idea {
    module {
        inheritOutputDirs = true
    }
}

