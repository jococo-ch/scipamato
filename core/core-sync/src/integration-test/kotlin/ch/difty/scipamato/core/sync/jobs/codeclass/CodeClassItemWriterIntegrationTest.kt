package ch.difty.scipamato.core.sync.jobs.codeclass

import ch.difty.scipamato.core.sync.PublicCodeClass
import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest
import ch.difty.scipamato.core.sync.newPublicCodeClass
import ch.difty.scipamato.publ.db.tables.CodeClass.CODE_CLASS
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

private const val ID_EXISTING = 1
private const val ID_NEW = -1
private const val LANG_CODE = "en"

internal class CodeClassItemWriterIntegrationTest : AbstractItemWriterIntegrationTest() {

    private val writer by lazy { CodeClassItemWriter(dsl) }

    @Test
    fun insertingNewCodeClass() {
        fetch(ID_NEW).shouldBeNull()

        writer.writeOne(
            newPublicCodeClass(ID_NEW, LANG_CODE, "CC$ID_NEW", "CC$ID_NEW", null, null, null, now())
        )

        fetch(ID_NEW).shouldNotBeNull().name shouldBeEqualTo "CC$ID_NEW"
    }

    @Test
    fun updatingExistingCodeClass() {
        val existing = fetch(ID_EXISTING).shouldNotBeNull()
        existing.description.shouldNotBeNull().shouldBeEmpty()
        existing.description = "foo"

        writer.writeOne(existing)

        fetch(ID_EXISTING).shouldNotBeNull().description shouldBeEqualTo "foo"
    }

    private fun now() = LocalDateTime.now()

    private fun fetch(id: Int): PublicCodeClass? = dsl
        .selectFrom(CODE_CLASS)
        .where(CODE_CLASS.CODE_CLASS_ID.eq(id))
        .and(CODE_CLASS.LANG_CODE.eq(LANG_CODE))
        .fetchOneInto(PublicCodeClass::class.java)
}
