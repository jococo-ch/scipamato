package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.PublicNewsletter
import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest
import ch.difty.scipamato.core.sync.newPublicNewsletter
import ch.difty.scipamato.publ.db.tables.Newsletter.NEWSLETTER
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

private const val ID_EXISTING = 1
private const val ID_NEW = -1

internal class NewsletterItemWriterIntegrationTest : AbstractItemWriterIntegrationTest() {

    private val writer by lazy { NewsletterItemWriter(dsl) }

    @Test
    fun insertingNewNewsletter() {
        fetch(ID_NEW).shouldBeNull()

        writer.writeOne(
            newPublicNewsletter(
                ID_NEW, "issue", LocalDate.parse("2018-06-14"), null, null, null, LocalDateTime.now()
            )
        )

        fetch(ID_NEW).shouldNotBeNull().issue shouldBeEqualTo "issue"
    }

    @Test
    fun updatingExistingNewsletter() {
        val existing = fetch(ID_EXISTING).shouldNotBeNull()
        existing.issue shouldBeEqualTo "2018/04"
        existing.issue = "foo"

        writer.writeOne(existing)

        fetch(ID_EXISTING).shouldNotBeNull().issue shouldBeEqualTo "foo"
    }

    private fun fetch(id: Int): PublicNewsletter? = dsl
        .selectFrom(NEWSLETTER)
        .where(NEWSLETTER.ID.eq(id))
        .fetchOneInto(PublicNewsletter::class.java)
}
