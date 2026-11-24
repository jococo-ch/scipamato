package ch.difty.scipamato.core.web.common

import ch.difty.scipamato.core.web.WicketTest
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldBeGreaterOrEqualTo
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.Test

internal class BasePageJQueryCompatTest : WicketTest() {

    @Test
    fun page_rendersTheJQueryCompatScript_afterJQuery() {
        tester.startPage(object : BasePage<Any?>(PageParameters()) {
            private val serialVersionUID: Long = 1L
        })

        val html = tester.lastResponseAsString
        val jQuery = html.indexOf("jquery/jquery-4")
        val compat = html.indexOf("js/jquery-compat")
        jQuery shouldBeGreaterOrEqualTo 0
        compat shouldBeGreaterThan jQuery
    }
}
