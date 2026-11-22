package ch.difty.scipamato.core.sync.jobs.language

import ch.difty.scipamato.core.sync.PublicLanguage
import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest
import ch.difty.scipamato.core.sync.newPublicLanguage
import ch.difty.scipamato.publ.db.tables.Language.LANGUAGE
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import java.sql.Timestamp

private const val CODE_EXISTING = "de"
private const val CODE_NEW = "es"

internal class LanguageItemWriterIntegrationTest : AbstractItemWriterIntegrationTest() {

    private val writer by lazy { LanguageItemWriter(dsl) }
    private val synched = Timestamp.valueOf("2026-01-02 03:04:05")

    @Test
    fun insertingNewLanguage() {
        fetch(CODE_NEW).shouldBeNull()

        writer.writeOne(newPublicLanguage(CODE_NEW, synched, false))

        fetch(CODE_NEW).shouldNotBeNull().mainLanguage.shouldNotBeNull().shouldBeFalse()
    }

    @Test
    fun updatingExistingLanguage() {
        val existing = fetch(CODE_EXISTING).shouldNotBeNull()
        existing.lastSynched shouldNotBeEqualTo synched
        existing.lastSynched = synched

        writer.writeOne(existing)

        fetch(CODE_EXISTING).shouldNotBeNull().lastSynched shouldBeEqualTo synched
    }

    private fun fetch(code: String): PublicLanguage? = dsl
        .selectFrom(LANGUAGE)
        .where(LANGUAGE.CODE.eq(code))
        .fetchOneInto(PublicLanguage::class.java)
}
