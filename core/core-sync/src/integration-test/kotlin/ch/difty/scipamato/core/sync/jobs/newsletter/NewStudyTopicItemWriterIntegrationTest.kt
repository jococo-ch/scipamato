package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.PublicNewStudyTopic
import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest
import ch.difty.scipamato.core.sync.newPublicNewStudyTopic
import ch.difty.scipamato.publ.db.tables.NewStudyTopic.NEW_STUDY_TOPIC
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.LocalDateTime

private const val NEWSLETTER_ID = 2
private const val TOPIC_ID_EXISTING = 2
private const val TOPIC_ID_NEW = 4
private const val SORT_EXISTING = 1
private const val SORT_CHANGED = 4

internal class NewStudyTopicItemWriterIntegrationTest : AbstractItemWriterIntegrationTest() {

    private val writer by lazy { NewStudyTopicItemWriter(dsl) }

    @Test
    fun insertingNewNewStudyTopic() {
        fetch(TOPIC_ID_NEW).shouldBeNull()

        writer.writeOne(
            newPublicNewStudyTopic(
                NEWSLETTER_ID, TOPIC_ID_NEW, SORT_EXISTING, null, null, null, Timestamp.valueOf(LocalDateTime.now())
            )
        )

        fetch(TOPIC_ID_NEW).shouldNotBeNull().sort shouldBeEqualTo SORT_EXISTING
    }

    @Test
    fun updatingExistingNewStudyTopic() {
        val existing = fetch(TOPIC_ID_EXISTING).shouldNotBeNull()
        existing.sort shouldBeEqualTo SORT_EXISTING
        existing.sort = SORT_CHANGED

        writer.writeOne(existing)

        fetch(TOPIC_ID_EXISTING).shouldNotBeNull().sort shouldBeEqualTo SORT_CHANGED
    }

    private fun fetch(topicId: Int): PublicNewStudyTopic? = dsl
        .selectFrom(NEW_STUDY_TOPIC)
        .where(NEW_STUDY_TOPIC.NEWSLETTER_ID.eq(NEWSLETTER_ID))
        .and(NEW_STUDY_TOPIC.NEWSLETTER_TOPIC_ID.eq(topicId))
        .fetchOneInto(PublicNewStudyTopic::class.java)
}
