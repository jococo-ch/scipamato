package ch.difty.scipamato.core.sync

import org.flywaydb.core.Flyway
import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.postgresql.PostgreSQLContainer

/**
 * The scipamato-core and scipamato-public databases, each in its own PostgreSQL container,
 * migrated by Flyway with the migrations and sample data of core- and public-persistence-jooq.
 * Started once per JVM and shared by all integration tests.
 */
object SyncDatabases {

    private const val IMAGE = "postgres:17.4"

    private val core by lazy { migrated("scipamato", "scipamato.it.core.migrations") }
    private val public by lazy { migrated("scipamato_public", "scipamato.it.public.migrations") }

    // the database names must match the GRANTs in the migrations
    private fun migrated(dbName: String, locationsProperty: String): PostgreSQLContainer =
        PostgreSQLContainer(IMAGE).withDatabaseName(dbName).apply {
            start()
            Flyway.configure()
                .dataSource(jdbcUrl, username, password)
                .configuration(mapOf("flyway.locations" to requireNotNull(System.getProperty(locationsProperty))))
                .load()
                .migrate()
        }

    fun register(registry: DynamicPropertyRegistry) {
        registry.add("sync.source.datasource.jdbc-url") { core.jdbcUrl }
        registry.add("sync.batch.datasource.jdbc-url") { core.jdbcUrl }
        registry.add("sync.batch.datasource.username") { core.username }
        registry.add("sync.batch.datasource.password") { core.password }
        registry.add("sync.target.datasource.jdbc-url") { public.jdbcUrl }
        registry.add("sync.target.datasource.username") { public.username }
        registry.add("sync.target.datasource.password") { public.password }
    }
}
