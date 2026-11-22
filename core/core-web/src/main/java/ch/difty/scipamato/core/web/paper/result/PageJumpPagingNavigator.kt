package ch.difty.scipamato.core.web.paper.result

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.NumberTextField
import org.apache.wicket.markup.html.navigation.paging.IPageable
import org.apache.wicket.model.Model

/**
 * Paging navigator that, in addition to the page links, offers a field to jump to a page by its number.
 */
class PageJumpPagingNavigator(id: String, pageable: IPageable) : BootstrapPagingNavigator(id, pageable) {

    override fun onInitialize() {
        super.onInitialize()
        val pageNumber = NumberTextField("pageNumber", Model<Long>(), Long::class.javaObjectType)
        add(object : Form<Unit>("jumpForm") {
            override fun onSubmit() {
                pageNumber.modelObject?.let {
                    pageable.currentPage = (it - 1).coerceIn(0L, maxOf(pageable.pageCount - 1, 0L))
                }
            }

            private val serialVersionUID: Long = 1L
        }.apply { add(pageNumber) })
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
