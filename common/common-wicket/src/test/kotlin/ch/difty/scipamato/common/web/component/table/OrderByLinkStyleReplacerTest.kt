package ch.difty.scipamato.common.web.component.table

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState
import org.apache.wicket.markup.Markup
import org.apache.wicket.mock.MockApplication
import org.apache.wicket.util.tester.WicketTester

internal class OrderByLinkStyleReplacerTest : FunSpec({

    lateinit var tester: WicketTester

    beforeTest {
        tester = WicketTester(object : MockApplication() {
            override fun init() {
                super.init()
                componentInstantiationListeners.add(OrderByLinkStyleReplacer())
            }
        })
    }

    afterTest { tester.destroy() }

    fun renderLinkWithStyle(style: String): String {
        tester.startComponentInPage(
            OrderByLink("link", "property", SortStateLocator()),
            Markup.of("""<a wicket:id="link" style="$style" class="show"></a>"""),
        )
        return tester.lastResponseAsString
    }

    test("replaces text-decoration none with the Bootstrap utility class") {
        val html = renderLinkWithStyle("text-decoration: none;")

        html shouldNotContain "style="
        html shouldContain """class="show text-decoration-none""""
    }

    test("leaves any other inline style untouched") {
        val html = renderLinkWithStyle("color: red;")

        html shouldContain """style="color: red;""""
        html shouldNotContain "text-decoration-none"
    }
})

private class SortStateLocator : ISortStateLocator<String> {
    private val state = SingleSortState<String>()
    override fun getSortState(): ISortState<String> = state

    companion object {
        private const val serialVersionUID = 1L
    }
}
