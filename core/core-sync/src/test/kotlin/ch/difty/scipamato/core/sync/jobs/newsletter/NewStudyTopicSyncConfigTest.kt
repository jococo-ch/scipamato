package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.common.FrozenDateTimeService
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.tools.jdbc.MockConnection
import org.jooq.tools.jdbc.MockDataProvider
import org.junit.jupiter.api.Test
import org.springframework.batch.core.repository.JobRepository
import org.springframework.transaction.PlatformTransactionManager
import java.sql.ResultSet
import java.time.LocalDateTime
import javax.sql.DataSource

internal class NewStudyTopicSyncConfigTest {

    private val provider = mockk<MockDataProvider>()
    private val connection = MockConnection(provider)
    private val jooqCore: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val jooqPublic: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val coreDataSource = mockk<DataSource>()
    private val jobRepository = mockk<JobRepository>()
    private val transactionManager = mockk<PlatformTransactionManager>()
    private val dateTimeService = FrozenDateTimeService()

    private val config = NewStudyTopicSyncConfig(
        jooqCore,
        jooqPublic,
        coreDataSource,
        jobRepository,
        transactionManager,
        dateTimeService
    )

    @Test
    fun jobName() {
        config.jobName shouldBeEqualTo "syncNewStudyTopicJob"
    }

    @Test
    fun publicWriter() {
        config.publicWriter() shouldBeInstanceOf NewStudyTopicItemWriter::class
    }

    @Test
    fun lastSynchedField() {
        config.lastSynchedField().toString() shouldBeEqualTo """"public"."new_study_topic"."last_synched""""
    }

    @Test
    fun makingEntity() {
        val resultSet = mockk<ResultSet> {
            every { getInt("newsletter_id") } returns 1
            every { getInt("newsletter_topic_id") } returns 2
            every { getInt("sort") } returns 3
            every { getInt("version") } returns 4
            every { getObject("created", LocalDateTime::class.java) } returns LocalDateTime.parse("2020-04-04T12:13:14")
            every { getObject("NTTLM", LocalDateTime::class.java) } returns LocalDateTime.parse("2020-04-04T13:14:15")
            every { getObject("NNTLM", LocalDateTime::class.java) } returns LocalDateTime.parse("2020-04-04T13:14:16")
            every { wasNull() } returns false
        }

        val newStudyTopic = config.makeEntity(resultSet)

        newStudyTopic.newsletterId shouldBeEqualTo 1
        newStudyTopic.newsletterTopicId shouldBeEqualTo 2
        newStudyTopic.sort shouldBeEqualTo 3
        newStudyTopic.version shouldBeEqualTo 4
        newStudyTopic.created.toString() shouldBeEqualTo "2020-04-04T12:13:14"
        newStudyTopic.lastModified.toString() shouldBeEqualTo "2020-04-04T13:14:16"
        newStudyTopic.lastSynched.toString() shouldBeEqualTo "2016-12-09T06:02:13"
    }

    @Suppress("MaxLineLength")
    @Test
    fun selectSql() {
        /* ktlint-disable max-line-length */
        config.selectSql() shouldBeEqualTo
            """select "public"."paper_newsletter"."newsletter_id", "public"."newsletter_topic_tr"."newsletter_topic_id", "public"."newsletter_topic_tr"."version", "public"."newsletter_topic_tr"."created", "public"."newsletter_topic_tr"."last_modified" as "NTTLM", "public"."newsletter_newsletter_topic"."sort", "public"."newsletter_newsletter_topic"."last_modified" as "NNTLM" from "public"."paper_newsletter" join "public"."newsletter_topic" on "public"."paper_newsletter"."newsletter_topic_id" = "public"."newsletter_topic"."id" join "public"."newsletter_topic_tr" on "public"."newsletter_topic"."id" = "public"."newsletter_topic_tr"."newsletter_topic_id" left outer join "public"."newsletter_newsletter_topic" on ("public"."paper_newsletter"."newsletter_id" = "public"."newsletter_newsletter_topic"."newsletter_id" and "public"."newsletter_topic"."id" = "public"."newsletter_newsletter_topic"."newsletter_topic_id") join "public"."newsletter" on "public"."paper_newsletter"."newsletter_id" = "public"."newsletter"."id" where "public"."newsletter"."publication_status" = 1"""
        /* ktlint-enable max-line-length */
    }

    @Test
    fun pseudoFkDcs() {
        config.pseudoFkDcs.toString() shouldBeEqualTo
            """delete from "public"."new_study_topic"
            |where "public"."new_study_topic"."newsletter_topic_id" not in (
            |  select distinct "public"."newsletter_topic"."id"
            |  from "public"."newsletter_topic"
            |)""".trimMargin()
    }

    @Test
    fun makingEntity_withNullSort_usesMaxInt() {
        makeEntity(sortIsNull = true, nttLastModified = EARLIER, nntLastModified = EARLIER)
            .sort shouldBeEqualTo Int.MAX_VALUE
    }

    @Test
    fun makingEntity_withBothLastModifiedNull() {
        makeEntity(nttLastModified = null, nntLastModified = null).lastModified.shouldBeNull()
    }

    @Test
    fun makingEntity_withNttLastModifiedNull_usesNnt() {
        makeEntity(nttLastModified = null, nntLastModified = EARLIER).lastModified shouldBeEqualTo EARLIER
    }

    @Test
    fun makingEntity_withNntLastModifiedNull_usesNtt() {
        makeEntity(nttLastModified = EARLIER, nntLastModified = null).lastModified shouldBeEqualTo EARLIER
    }

    @Test
    fun makingEntity_withNttLastModifiedLater_usesNtt() {
        makeEntity(nttLastModified = LATER, nntLastModified = EARLIER).lastModified shouldBeEqualTo LATER
    }

    private fun makeEntity(sortIsNull: Boolean = false, nttLastModified: LocalDateTime?, nntLastModified: LocalDateTime?) =
        config.makeEntity(mockk(relaxed = true) {
            every { getInt("sort") } returns if (sortIsNull) 0 else 1
            every { wasNull() } returnsMany listOf(false, false, sortIsNull, false)
            every { getObject(any<String>(), LocalDateTime::class.java) } returns null
            every { getObject("NTTLM", LocalDateTime::class.java) } returns nttLastModified
            every { getObject("NNTLM", LocalDateTime::class.java) } returns nntLastModified
        })

    private companion object {
        val EARLIER: LocalDateTime = LocalDateTime.parse("2020-04-04T13:14:15")
        val LATER: LocalDateTime = LocalDateTime.parse("2020-04-04T13:14:16")
    }
}
