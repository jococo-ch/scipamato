package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.PublicNewStudy
import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest
import ch.difty.scipamato.core.sync.newPublicNewStudy
import ch.difty.scipamato.publ.db.tables.NewStudy.NEW_STUDY
import ch.difty.scipamato.publ.db.tables.Paper.PAPER
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

private const val NEWSLETTER_ID = 1
private const val TOPIC_ID = 1
private const val PAPER_NUMBER_EXISTING = 8924L
private const val PAPER_NUMBER_NEW = -10L
private const val SORT = 1
private const val YEAR = 2018

internal class NewStudyItemWriterIntegrationTest : AbstractItemWriterIntegrationTest() {

    private val writer by lazy { NewStudyItemWriter(dsl) }

    @Test
    fun insertingNewNewStudy() {
        fetch(PAPER_NUMBER_NEW).shouldBeNull()
        dsl.insertInto(PAPER)
            .columns(PAPER.ID, PAPER.NUMBER, PAPER.PUBLICATION_YEAR)
            .values(PAPER_NUMBER_NEW, PAPER_NUMBER_NEW, YEAR)
            .execute()

        writer.writeOne(
            newPublicNewStudy(
                NEWSLETTER_ID, PAPER_NUMBER_NEW, TOPIC_ID, SORT, YEAR, "authors", "headline", "description",
                null, null, null, LocalDateTime.now()
            )
        )

        fetch(PAPER_NUMBER_NEW).shouldNotBeNull().headline shouldBeEqualTo "headline"
    }

    @Test
    fun updatingExistingNewStudy() {
        val existing = fetch(PAPER_NUMBER_EXISTING).shouldNotBeNull()
        existing.authors shouldBeEqualTo "Di et al."
        existing.authors = "foo"

        writer.writeOne(existing)

        fetch(PAPER_NUMBER_EXISTING).shouldNotBeNull().authors shouldBeEqualTo "foo"
    }

    private fun fetch(paperNumber: Long): PublicNewStudy? = dsl
        .selectFrom(NEW_STUDY)
        .where(NEW_STUDY.NEWSLETTER_ID.eq(NEWSLETTER_ID))
        .and(NEW_STUDY.PAPER_NUMBER.eq(paperNumber))
        .fetchOneInto(PublicNewStudy::class.java)
}
