package ch.difty.scipamato.core.sync.launcher

import ch.difty.scipamato.core.sync.AbstractIntegrationTest
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Runs the whole synchronization from scipamato-core to scipamato-public on the
 * Testcontainers databases of the integration tests. Not part of the build; run it
 * explicitly with `./gradlew :core-sync:adhocTest`.
 */
internal class RefDataSyncJobLauncherAdHocTest @Autowired constructor(
    private val launcher: RefDataSyncJobLauncher,
) : AbstractIntegrationTest() {

    @Test
    fun run() {
        val failures = mutableListOf<String>()
        var done = false

        launcher.launch(
            setSuccess = { println("SUCCESS $it") },
            setFailure = { failures += it },
            setWarning = { println("WARNING $it") },
            setDone = { done = true },
        )

        failures.shouldBeEmpty()
        done.shouldBeTrue()
    }
}
