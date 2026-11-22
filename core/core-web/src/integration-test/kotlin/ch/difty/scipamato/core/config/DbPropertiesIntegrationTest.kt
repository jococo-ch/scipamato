package ch.difty.scipamato.core.config

import ch.difty.scipamato.core.AbstractIntegrationTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class DbPropertiesIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var dbProperties: DbProperties

    @Test
    fun schema_hasDefaultValuePublic() {
        dbProperties.schema shouldBeEqualTo "public"
    }
}
