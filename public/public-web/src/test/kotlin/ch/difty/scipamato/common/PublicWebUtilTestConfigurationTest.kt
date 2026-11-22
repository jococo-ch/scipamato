package ch.difty.scipamato.common

import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class PublicWebUtilTestConfigurationTest {

    @Test
    fun frozenDateTimeService() {
        val uc = PublicWebUtilTestConfiguration()
        uc.frozenDateTimeService() shouldBeInstanceOf FrozenDateTimeService::class
    }
}
