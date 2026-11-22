package ch.difty.scipamato.core.sync

import com.zaxxer.hikari.HikariDataSource
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.batch.jdbc.autoconfigure.BatchDataSourceScriptDatabaseInitializer
import org.springframework.test.util.ReflectionTestUtils
import javax.sql.DataSource

internal class BatchConfigurationIntegrationTest @Autowired constructor(
    private val initializer: BatchDataSourceScriptDatabaseInitializer,
) : AbstractIntegrationTest() {

    @Test
    fun batchSchemaIsInitializedThroughTheBatchDataSource() {
        // the initializer does not expose its data source
        val dataSource = ReflectionTestUtils.getField(initializer, "dataSource") as DataSource
        dataSource.unwrap(HikariDataSource::class.java).poolName shouldBeEqualTo "SciPaMaTo-Batch-HikariCP"
    }
}
