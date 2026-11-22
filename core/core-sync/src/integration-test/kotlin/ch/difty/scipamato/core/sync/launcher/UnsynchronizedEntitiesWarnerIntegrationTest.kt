package ch.difty.scipamato.core.sync.launcher

import ch.difty.scipamato.core.sync.AbstractIntegrationTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class UnsynchronizedEntitiesWarnerIntegrationTest @Autowired constructor(
    private val warner: Warner,
) : AbstractIntegrationTest() {

    @Test
    fun findUnsynchronizedPapers() {
        warner.findUnsynchronizedPapers() shouldBeEqualTo
            "Papers not synchronized due to missing codes: Number 2, 3, 4, 11."
    }
}
