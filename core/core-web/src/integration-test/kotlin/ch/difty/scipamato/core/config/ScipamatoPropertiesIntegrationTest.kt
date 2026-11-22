package ch.difty.scipamato.core.config

import ch.difty.scipamato.core.AbstractIntegrationTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class ScipamatoPropertiesIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var sp: ScipamatoProperties

    @Test
    fun brand_hasDefaultValue() {
        sp.brand shouldBeEqualTo "SciPaMaTo"
    }

    @Test
    fun defaultLocalization_hasDefaultEnglish() {
        sp.defaultLocalization shouldBeEqualTo "de"
    }

    @Test
    fun pubmedBaseUrl_hasDefaultValue() {
        sp.pubmedBaseUrl shouldBeEqualTo "https://www.ncbi.nlm.nih.gov/pubmed/"
    }

    @Test
    fun authorParser_isDefault() {
        sp.authorParser shouldBeEqualTo "PUBMED"
    }

    @Test
    fun paperMinimumToBeRecycled_hasDefaultValue() {
        sp.paperNumberMinimumToBeRecycled shouldBeEqualTo 8
    }

    @Test
    fun dbSchema_hasValuePublic() {
        sp.dbSchema shouldBeEqualTo "public"
    }

    @Test
    fun gettingMultiSelectBoxActionBoxWithMoreEntriesThan_hasDefaultValue4() {
        sp.multiSelectBoxActionBoxWithMoreEntriesThan shouldBeEqualTo 4
    }

    @Test
    fun gettingPubmedApiKey_hasNoDefaultValue() {
        sp.pubmedApiKey.shouldBeNull()
    }
}
