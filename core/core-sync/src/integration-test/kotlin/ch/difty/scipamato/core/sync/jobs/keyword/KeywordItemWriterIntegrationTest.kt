package ch.difty.scipamato.core.sync.jobs.keyword

import ch.difty.scipamato.core.sync.PublicKeyword
import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest
import ch.difty.scipamato.core.sync.newPublicKeyword
import ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.LocalDateTime

private const val ID_EXISTING = 8
private const val ID_NEW = 10
private const val LANG_CODE = "en"

internal class KeywordItemWriterIntegrationTest : AbstractItemWriterIntegrationTest() {

    private val writer by lazy { KeywordItemWriter(dsl) }

    @Test
    fun insertingNewKeyword() {
        fetch(ID_NEW).shouldBeNull()

        writer.writeOne(
            newPublicKeyword(
                ID_NEW, ID_NEW, LANG_CODE, "foo", null, null, null, Timestamp.valueOf(LocalDateTime.now()), null
            )
        )

        fetch(ID_NEW).shouldNotBeNull().name shouldBeEqualTo "foo"
    }

    @Test
    fun updatingExistingKeyword() {
        val existing = fetch(ID_EXISTING).shouldNotBeNull()
        existing.name shouldBeEqualTo "Allergies"
        existing.searchOverride.shouldBeNull()
        existing.searchOverride = "bar"

        writer.writeOne(existing)

        fetch(ID_EXISTING).shouldNotBeNull().searchOverride shouldBeEqualTo "bar"
    }

    private fun fetch(id: Int): PublicKeyword? = dsl
        .selectFrom(KEYWORD)
        .where(KEYWORD.ID.eq(id))
        .fetchOneInto(PublicKeyword::class.java)
}
