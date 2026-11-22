package ch.difty.scipamato.core.sync.jobs.code

import ch.difty.scipamato.core.sync.PublicCode
import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest
import ch.difty.scipamato.core.sync.newPublicCode
import ch.difty.scipamato.publ.db.tables.Code.CODE
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.LocalDateTime

private const val CODE_EXISTING = "1F"
private const val CODE_NEW = "9X"
private const val LANG_CODE = "en"
private const val CODE_CLASS_NEW = 8
private const val SORT_NEW = 100

internal class CodeItemWriterIntegrationTest : AbstractItemWriterIntegrationTest() {

    private val writer by lazy { CodeItemWriter(dsl) }

    @Test
    fun insertingNewCode() {
        fetch(CODE_NEW).shouldBeNull()

        writer.writeOne(
            newPublicCode(
                CODE_NEW, LANG_CODE, CODE_CLASS_NEW, CODE_NEW, null, SORT_NEW, null, null, null, Timestamp.valueOf(LocalDateTime.now())
            )
        )

        fetch(CODE_NEW).shouldNotBeNull().name shouldBeEqualTo CODE_NEW
    }

    @Test
    fun updatingExistingCode() {
        val existing = fetch(CODE_EXISTING).shouldNotBeNull()
        existing.codeClassId shouldBeEqualTo 1
        existing.codeClassId = 2

        writer.writeOne(existing)

        fetch(CODE_EXISTING).shouldNotBeNull().codeClassId shouldBeEqualTo 2
    }

    private fun fetch(code: String): PublicCode? = dsl
        .selectFrom(CODE)
        .where(CODE.CODE_.eq(code))
        .and(CODE.LANG_CODE.eq(LANG_CODE))
        .fetchOneInto(PublicCode::class.java)
}
