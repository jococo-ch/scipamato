package ch.difty.scipamato.core.sync.jobs.newstudypagelink

import ch.difty.scipamato.core.sync.PublicNewStudyPageLink
import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest
import ch.difty.scipamato.core.sync.newPublicNewStudyPageLink
import ch.difty.scipamato.publ.db.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.LocalDateTime

private const val LANG_CODE = "de"
private const val SORT_EXISTING = 1
private const val SORT_NEW = 20
private const val TITLE_EXISTING = "Web Suche"

internal class NewStudyPageLinkItemWriterIntegrationTest : AbstractItemWriterIntegrationTest() {

    private val writer by lazy { NewStudyPageLinkItemWriter(dsl) }

    @Test
    fun insertingNewNewStudyPageLink() {
        fetch(SORT_NEW).shouldBeNull()

        writer.writeOne(
            newPublicNewStudyPageLink(
                LANG_CODE, SORT_NEW, "foo", "https://a.b", null, null, null, Timestamp.valueOf(LocalDateTime.now())
            )
        )

        fetch(SORT_NEW).shouldNotBeNull().url shouldBeEqualTo "https://a.b"
    }

    @Test
    fun updatingExistingNewStudyPageLink() {
        val existing = fetch(SORT_EXISTING).shouldNotBeNull()
        existing.title shouldBeEqualTo TITLE_EXISTING
        existing.title = "${TITLE_EXISTING}XX"

        writer.writeOne(existing)

        fetch(SORT_EXISTING).shouldNotBeNull().title shouldBeEqualTo "${TITLE_EXISTING}XX"
    }

    private fun fetch(sort: Int): PublicNewStudyPageLink? = dsl
        .selectFrom(NEW_STUDY_PAGE_LINK)
        .where(NEW_STUDY_PAGE_LINK.LANG_CODE.eq(LANG_CODE))
        .and(NEW_STUDY_PAGE_LINK.SORT.eq(sort))
        .fetchOneInto(PublicNewStudyPageLink::class.java)
}
