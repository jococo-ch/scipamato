package ch.difty.scipamato.core.sync.jobs.paper

import ch.difty.scipamato.core.sync.PublicPaper
import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest
import ch.difty.scipamato.core.sync.newPublicPaper
import ch.difty.scipamato.publ.db.tables.Paper.PAPER
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.LocalDateTime

private const val NUMBER_EXISTING = 1L
private const val NUMBER_NEW = -10L
private const val ID_CHANGED = -2L

internal class PaperItemWriterIntegrationTest : AbstractItemWriterIntegrationTest() {

    private val writer by lazy { PaperItemWriter(dsl) }

    @Test
    @Suppress("ArrayPrimitive") // newPublicPaper takes the Short[] of the jOOQ pojo
    fun insertingNewPaper() {
        fetch(NUMBER_NEW).shouldBeNull()

        writer.writeOne(
            newPublicPaper(
                NUMBER_NEW, NUMBER_NEW, null, null, null, null, null, null, null, null, null, null,
                null, null, null, arrayOf(1, 2), arrayOf(), arrayOf("F1"), Timestamp.valueOf(LocalDateTime.now())
            )
        )

        fetch(NUMBER_NEW).shouldNotBeNull().codes.shouldNotBeNull().toList() shouldBeEqualTo listOf("F1")
    }

    @Test
    fun updatingExistingPaper() {
        val existing = fetch(NUMBER_EXISTING).shouldNotBeNull()
        existing.id shouldBeEqualTo NUMBER_EXISTING
        existing.id = ID_CHANGED

        writer.writeOne(existing)

        fetch(NUMBER_EXISTING).shouldNotBeNull().id shouldBeEqualTo ID_CHANGED
    }

    private fun fetch(number: Long): PublicPaper? = dsl
        .selectFrom(PAPER)
        .where(PAPER.NUMBER.eq(number))
        .fetchOneInto(PublicPaper::class.java)
}
