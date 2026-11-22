package ch.difty.scipamato.core.sync.jobs

import ch.difty.scipamato.core.sync.AbstractIntegrationTest
import com.zaxxer.hikari.HikariDataSource
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldHaveSize
import org.jooq.Configuration
import org.jooq.SQLDialect
import org.jooq.impl.DataSourceConnectionProvider
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.jooq.autoconfigure.ExceptionTranslatorExecuteListener
import org.springframework.boot.jooq.autoconfigure.SpringTransactionProvider
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy

internal class JooqConfigurationIntegrationTest @Autowired constructor(
    @Qualifier("publicConfiguration") private val jooqPublicConfig: Configuration,
    @Qualifier("coreConfiguration") private val jooqCoreConfig: Configuration,
    @Qualifier("batchConfiguration") private val jooqBatchConfig: Configuration,
) : AbstractIntegrationTest() {

    @Test
    fun jooqPublicConfigIsProperlyWired() = assertConfiguration(jooqPublicConfig)

    @Test
    fun jooqCoreConfigIsProperlyWired() = assertConfiguration(jooqCoreConfig)

    @Test
    fun jooqBatchConfigIsProperlyWired() = assertConfiguration(jooqBatchConfig)

    private fun assertConfiguration(config: Configuration) {
        config.dialect() shouldBeEqualTo SQLDialect.POSTGRES

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
