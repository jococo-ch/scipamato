package ch.difty.scipamato.core.web.paper.result

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator
import org.apache.wicket.AttributeModifier
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.NumberTextField
import org.apache.wicket.markup.html.navigation.paging.IPageable
import org.apache.wicket.model.Model
import org.apache.wicket.model.StringResourceModel

/**
 * Paging navigator that, in addition to the page links, offers a field to jump to a page by its number.
 */
class PageJumpPagingNavigator(id: String, pageable: IPageable) : BootstrapPagingNavigator(id, pageable) {

    override fun onInitialize() {
        super.onInitialize()
        val pageNumber = NumberTextField("pageNumber", Model<Long>(), Long::class.javaObjectType).apply {
            add(AttributeModifier.replace("placeholder", StringResourceModel("pageNumber.placeholder", this)))
            add(AttributeModifier.replace("title", StringResourceModel("pageNumber.title", this)))
        }
        add(object : Form<Unit>("jumpForm") {
            override fun onSubmit() {
                pageNumber.modelObject?.let { jumpTo(it) }
            }

            private val serialVersionUID: Long = 1L
        }.apply { add(pageNumber) })
    }

    private fun jumpTo(pageNumber: Long) {
        val lastPageIndex = maxOf(pageable.pageCount - 1, 0L)
        pageable.currentPage = (pageNumber - 1).coerceIn(0L, lastPageIndex)
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
