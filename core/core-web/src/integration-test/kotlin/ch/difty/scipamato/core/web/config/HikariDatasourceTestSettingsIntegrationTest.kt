package ch.difty.scipamato.core.web.config

import ch.difty.scipamato.core.AbstractIntegrationTest
import com.zaxxer.hikari.HikariDataSource
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import javax.sql.DataSource

internal class HikariDatasourceTestSettingsIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    @Qualifier("hikariBatchDataSource")
    private lateinit var batchDataSource: DataSource

    @Autowired
    @Qualifier("hikariPublicDataSource")
    private lateinit var publicDataSource: DataSource

    @Autowired
    @Qualifier("hikariCoreDataSource")
    private lateinit var coreDataSource: DataSource

    @Test
    fun validateSettingsBatchDatasource() {
        batchDataSource shouldBeInstanceOf HikariDataSource::class
        val ds = batchDataSource as HikariDataSource

        ds.jdbcUrl shouldBeEqualTo "jdbc:tc:postgresql:17.4:///scipamato?TC_DAEMON=true"
        ds.username shouldBeEqualTo "scipamadmin"
        ds.password shouldBeEqualTo "scipamadmin"
        ds.connectionTimeout shouldBeEqualTo 10000
        ds.idleTimeout shouldBeEqualTo 600000
        ds.maxLifetime shouldBeEqualTo 1800000
        ds.minimumIdle shouldBeEqualTo -1
        ds.maximumPoolSize shouldBeEqualTo 2
        ds.poolName shouldBeEqualTo "SciPaMaTo-Batch-HikariCP"
        ds.isReadOnly.shouldBeFalse()
    }

    @Test
    fun validateSettingsTargetDatasource() {
        publicDataSource shouldBeInstanceOf HikariDataSource::class
        val ds = publicDataSource as HikariDataSource

        ds.jdbcUrl shouldBeEqualTo "jdbc:tc:postgresql:17.4:///scipamato_public?TC_DAEMON=true"
        ds.username shouldBeEqualTo "scipamadminpub"
        ds.password shouldBeEqualTo "scipamadminpub"
        ds.connectionTimeout shouldBeEqualTo 10000
        ds.idleTimeout shouldBeEqualTo 600000
        ds.maxLifetime shouldBeEqualTo 1800000
        ds.minimumIdle shouldBeEqualTo -1
        ds.maximumPoolSize shouldBeEqualTo 3
        ds.poolName shouldBeEqualTo "SciPaMaTo-Target-HikariCP"
        ds.isReadOnly.shouldBeFalse()
    }

    @Test
    fun validateSettingsSourceDatasource() {
        coreDataSource shouldBeInstanceOf HikariDataSource::class
        val ds = coreDataSource as HikariDataSource

        ds.jdbcUrl shouldBeEqualTo "jdbc:tc:postgresql:17.4:///scipamato?TC_DAEMON=true"
        ds.username shouldBeEqualTo "scipamato"
        ds.password shouldBeEqualTo "scipamato"
        ds.connectionTimeout shouldBeEqualTo 10000
        ds.idleTimeout shouldBeEqualTo 600000
        ds.maxLifetime shouldBeEqualTo 1800000
        ds.minimumIdle shouldBeEqualTo 2
        ds.maximumPoolSize shouldBeEqualTo 2
        ds.poolName shouldBeEqualTo "SciPaMaTo-Source-HikariCP"
        ds.isReadOnly.shouldBeFalse()
    }
}
