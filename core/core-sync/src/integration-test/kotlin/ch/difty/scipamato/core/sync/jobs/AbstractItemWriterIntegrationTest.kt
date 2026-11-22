package ch.difty.scipamato.core.sync.jobs

import ch.difty.scipamato.core.sync.AbstractIntegrationTest
import org.jooq.DSLContext
import org.springframework.batch.infrastructure.item.Chunk
import org.springframework.batch.infrastructure.item.ItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.transaction.annotation.Transactional

// the writers write to scipamato-public; the default transaction manager would not roll that back
@Transactional(transactionManager = "publicTransactionManager")
@Suppress("UnnecessaryAbstractClass") // carries the transaction setup for subclasses
abstract class AbstractItemWriterIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    @Qualifier("publicDslContext")
    protected lateinit var dsl: DSLContext

    protected fun <T : Any> ItemWriter<T>.writeOne(item: T) = write(Chunk(listOf(item)))
}
