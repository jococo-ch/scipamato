package ch.difty.scipamato.core.web.paper.result

import de.agilecoders.wicket.core.Bootstrap
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider
import org.apache.wicket.markup.Markup
import org.apache.wicket.mock.MockApplication
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.util.tester.TagTester
import org.apache.wicket.util.tester.WicketTester
import java.io.Serializable
import java.util.Locale

internal class PageJumpNavigationToolbarTest : FunSpec({

    lateinit var tester: WicketTester

    beforeTest {
        tester = WicketTester(object : MockApplication() {
            override fun init() {
                super.init()
                Bootstrap.install(this)
            }
        })
    }

    afterTest { tester.destroy() }

    fun startTable(): DataTable<Item, String> {
        val table = newTable()
        tester.startComponentInPage(table, Markup.of("""<table wicket:id="$TABLE_ID"></table>"""))
        return table
    }

    fun jumpTo(pageNumber: String) {
        val formTester = tester.newFormTester(FORM_PATH)
        formTester.setValue("pageNumber", pageNumber)
        formTester.submit()
    }

    test("submitting a page number shows that page") {
        val table = startTable()

        jumpTo("3")

        table.currentPage shouldBe 2L
    }

    context("the page number field is labelled in the session language") {
        withData(
            nameFn = { (locale, placeholder, title) -> "$locale: $placeholder / $title" },
            Triple(Locale.ENGLISH, "Page", "Jump to page"),
            Triple(Locale.GERMAN, "Seite", "Zu Seite springen"),
        ) { (locale, placeholder, title) ->
            tester.session.locale = locale

            startTable()

            val field = TagTester.createTagByAttribute(tester.lastResponseAsString, "name", "pageNumber")
            field.getAttribute("placeholder") shouldBe placeholder
            field.getAttribute("title") shouldBe title

            val label = TagTester.createTagByName(tester.lastResponseAsString, "label")
            label.getAttribute("for") shouldBe field.getAttribute("id")
            label.value shouldBe title
        }
    }

    context("a page number out of range is limited to the existing pages") {
        withData(
            nameFn = { (input, expectedPageIndex) -> "$input -> page index $expectedPageIndex" },
            "0" to 0L,
            "-3" to 0L,
            "6" to 4L,
            "99" to 4L,
        ) { (input, expectedPageIndex) ->
            val table = startTable()

            jumpTo(input)

            table.currentPage shouldBe expectedPageIndex
        }
    }
})

private const val TABLE_ID = "table"
private const val ROWS_PER_PAGE = 10L
private const val ITEM_COUNT = 45
private const val FORM_PATH = "$TABLE_ID:topToolbars:toolbars:1:span:navigator:jumpForm"

internal data class Item(val name: String) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}

private class ItemProvider : SortableDataProvider<Item, String>() {
    private val items = (1..ITEM_COUNT).map { Item("item$it") }
    override fun iterator(first: Long, count: Long): Iterator<Item> =
        items.drop(first.toInt()).take(count.toInt()).iterator()

    override fun size(): Long = items.size.toLong()
    override fun model(item: Item): IModel<Item> = Model.of(item)

    companion object {
        private const val serialVersionUID = 1L
    }
}

private fun newTable(): DataTable<Item, String> {
    val columns = listOf<IColumn<Item, String>>(PropertyColumn(Model.of("name"), "name"))
    return DataTable(TABLE_ID, columns, ItemProvider(), ROWS_PER_PAGE).apply {
        addTopToolbar(PageJumpNavigationToolbar(this))
    }
}
