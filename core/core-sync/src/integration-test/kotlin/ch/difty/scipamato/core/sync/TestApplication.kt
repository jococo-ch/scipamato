package ch.difty.scipamato.core.sync

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.FrozenDateTimeService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@SpringBootApplication
open class TestApplication {

    @Bean
    @Primary
    open fun frozenDateTimeService(): DateTimeService = FrozenDateTimeService()
}
