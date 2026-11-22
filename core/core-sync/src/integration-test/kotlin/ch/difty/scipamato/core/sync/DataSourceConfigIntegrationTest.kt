package ch.difty.scipamato.core.sync

import ch.difty.scipamato.core.sync.launcher.Warner
import com.zaxxer.hikari.HikariDataSource
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeNull
import org.jooq.Configuration
import org.jooq.SQLDialect
import org.jooq.impl.DataSourceConnectionProvider
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.jooq.autoconfigure.ExceptionTranslatorExecuteListener
import org.springframework.boot.jooq.autoconfigure.SpringTransactionProvider
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy

internal class DataSourceConfigIntegrationTest @Autowired constructor(
    @Qualifier("coreConfiguration") private val coreConfiguration: Configuration,
    @Qualifier("publicConfiguration") private val publicConfiguration: Configuration,
    @Qualifier("batchConfiguration") private val batchConfiguration: Configuration,
    private val warner: Warner,
) : AbstractIntegrationTest() {

    @Test
    fun coreConfigIsProperlyWired() = assertConfiguration(coreConfiguration)

    @Test
    fun publicConfigIsProperlyWired() = assertConfiguration(publicConfiguration)

    @Test
    fun batchConfigIsProperlyWired() = assertConfiguration(batchConfiguration)

    @Test
    fun warnerIsWired() {
        warner.shouldNotBeNull()
    }

    private fun assertConfiguration(config: Configuration) {
        config.dialect() shouldBeEqualTo SQLDialect.POSTGRES
        config.settings().isExecuteWithOptimisticLocking.shouldNotBeNull().shouldBeTrue()

        val connectionProvider = config.connectionProvider()
        connectionProvider shouldBeInstanceOf DataSourceConnectionProvider::class
        val dataSource = (connectionProvider as DataSourceConnectionProvider).dataSource()
        dataSource shouldBeInstanceOf TransactionAwareDataSourceProxy::class
        dataSource.isWrapperFor(HikariDataSource::class.java).shouldBeTrue()

        config.executeListenerProviders() shouldHaveSize 2
        config.executeListenerProviders()[0].provide() shouldBeInstanceOf ExceptionTranslatorExecuteListener::class

        config.transactionProvider() shouldBeInstanceOf SpringTransactionProvider::class
    }
}
