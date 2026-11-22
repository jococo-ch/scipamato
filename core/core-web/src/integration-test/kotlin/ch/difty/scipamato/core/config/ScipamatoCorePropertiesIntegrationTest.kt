package ch.difty.scipamato.core.config

import ch.difty.scipamato.core.AbstractIntegrationTest
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldMatch
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Derives the configured values from the main `application.properties`.
 */
internal class ScipamatoCorePropertiesIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var appProperties: ApplicationCoreProperties

    @Test
    fun gettingBuildVersion() {
        appProperties.buildVersion.shouldNotBeNull() shouldMatch """\d+\.\d+\.\d+.*"""
    }

    @Test
    fun gettingDefaultStrategy() {
        appProperties.authorParserStrategy shouldBeEqualTo AuthorParserStrategy.PUBMED
    }

    @Test
    fun assertDefaultLocalization() {
        appProperties.defaultLocalization shouldBeEqualTo "de"
    }

    @Test
    fun assertBrand() {
        appProperties.brand shouldBeEqualTo "SciPaMaTo"
    }

    @Test
    fun assertMinimumPaperNumberToBeRecycled() {
        appProperties.minimumPaperNumberToBeRecycled shouldBeEqualTo 8L
    }

    @Test
    fun assertPubmedBaseUrl() {
        appProperties.pubmedBaseUrl shouldBeEqualTo "https://www.ncbi.nlm.nih.gov/pubmed/"
    }
}
