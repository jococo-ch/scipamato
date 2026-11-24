package ch.difty.scipamato.core.web.common

import ch.difty.scipamato.core.web.WicketTest
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotContain
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.Test

internal class BasePageFontAwesomeTest : WicketTest() {

    @Test
    fun page_rendersFontAwesomeFromWebjar_notFromCdn() {
        tester.startPage(object : BasePage<Any?>(PageParameters()) {
            private val serialVersionUID: Long = 1L
        })

        tester.lastResponseAsString shouldContain "webjars/font-awesome/"
        tester.lastResponseAsString shouldNotContain "use.fontawesome.com"
    }
}
