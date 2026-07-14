description = "SciPaMaTo-Common :: Wicket Project"

plugins {
    `java-library`
    `java-test-fixtures`
    `maven-publish`
}

/**
 * Make the static wicket resources that reside next to the kotlin classes in src{main,test} available.
 */
sourceSets {
    /** main: html, css, properties files */
    main {
        resources {
            setSrcDirs(setOf("src/main/kotlin"))
        }
    }
    /** test: html and properties files */
    test {
        resources {
            setSrcDirs(setOf("src/test/resources", "src/test/kotlin"))
        }
    }
}

dependencies {
    api(libs.spring.boot.starter.actuator)
    api(libs.spring.boot.starter.security)
    api(libs.spring.boot.admin.starter.client)
    api(libs.spring.core)
    api(libs.spring.boot.starter.web)
    api(libs.spring.boot.starter.wicket)
    api(libs.wicket.core)
    api(libs.wicket.ioc)
    api(libs.wicket.extensions)
    api(libs.wicket.request)
    api(libs.wicket.spring)
    api(libs.wicket.authroles)
    api(libs.wicket.beanvalidation)
    api(libs.hibernate.validator)
    api(libs.wicket.devutils)
    api(libs.wicket.tester)
    api(libs.wicketstuff.annotation)
    api(libs.wicketBootstrap.core)
    api(libs.wicketBootstrap.extensions)
    api(libs.wicketBootstrap.sass)
    api(libs.wicketBootstrap.themes)
    api(libs.fontAwesome)

    implementation(project(":common-utils"))
    implementation(project(":common-entity"))
    implementation(project(":common-persistence-api"))

    annotationProcessor(libs.spring.boot.configurationprocessor) {
        exclude("com.vaadin.external.google", "android-json")
    }

    testImplementation(libs.lombok)
    testImplementation(libs.jakarta.servletApi)
    testImplementation(libs.validationApi)

    testAnnotationProcessor(libs.lombok)

    testFixturesApi(testFixtures(project(":common-utils")))
}
