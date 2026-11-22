package ch.difty.scipamato.core.sync

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

// as in core-web, which runs the sync: the bean batchConfiguration in DataSourceConfig clashes with BatchConfiguration
@SpringBootTest(
    classes = [TestApplication::class],
    properties = ["spring.main.allow-bean-definition-overriding=true"],
)
@Suppress("UnnecessaryAbstractClass", "UtilityClassWithPublicConstructor") // carries the context setup for subclasses
abstract class AbstractIntegrationTest {
    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun databases(registry: DynamicPropertyRegistry) = SyncDatabases.register(registry)
    }
}
