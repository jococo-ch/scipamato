package ch.difty.scipamato.common

import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class CoreWebUtilTestConfigurationTest {

    @Test
    fun frozenDateTimeService() {
        val uc = CoreWebUtilTestConfiguration()
        uc.frozenDateTimeService() shouldBeInstanceOf FrozenDateTimeService::class
    }
}
