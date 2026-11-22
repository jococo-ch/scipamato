package ch.difty.scipamato.core.sync

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@SpringBootTest(classes = [TestApplication::class])
@Suppress("UnnecessaryAbstractClass", "UtilityClassWithPublicConstructor") // carries the context setup for subclasses
abstract class AbstractIntegrationTest {
    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun databases(registry: DynamicPropertyRegistry) = SyncDatabases.register(registry)
    }
}
