package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.PublicNewsletterTopic
import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest
import ch.difty.scipamato.core.sync.newPublicNewsletterTopic
import ch.difty.scipamato.publ.db.tables.NewsletterTopic.NEWSLETTER_TOPIC
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.LocalDateTime

private const val ID_EXISTING = 1
private const val ID_NEW = -1
private const val LANG_CODE = "de"

internal class NewsletterTopicItemWriterIntegrationTest : AbstractItemWriterIntegrationTest() {

    private val writer by lazy { NewsletterTopicItemWriter(dsl) }

    @Test
    fun insertingNewNewsletterTopic() {
        fetch(ID_NEW).shouldBeNull()

        writer.writeOne(
            newPublicNewsletterTopic(ID_NEW, LANG_CODE, "title", null, null, null, Timestamp.valueOf(LocalDateTime.now()))
        )

        fetch(ID_NEW).shouldNotBeNull().title shouldBeEqualTo "title"
    }

    @Test
    fun updatingExistingNewsletterTopic() {
        val existing = fetch(ID_EXISTING).shouldNotBeNull()
        existing.title shouldBeEqualTo "Tiefe Belastungen"
        existing.title = "foo"

        writer.writeOne(existing)

        fetch(ID_EXISTING).shouldNotBeNull().title shouldBeEqualTo "foo"
    }

    private fun fetch(id: Int): PublicNewsletterTopic? = dsl
        .selectFrom(NEWSLETTER_TOPIC)
        .where(NEWSLETTER_TOPIC.ID.eq(id))
        .and(NEWSLETTER_TOPIC.LANG_CODE.eq(LANG_CODE))
        .fetchOneInto(PublicNewsletterTopic::class.java)
}
