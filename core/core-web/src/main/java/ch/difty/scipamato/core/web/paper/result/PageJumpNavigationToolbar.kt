package ch.difty.scipamato.core.web.paper.result

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.toolbars.BootstrapNavigationToolbar
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable

/**
 * Navigation toolbar whose paging navigator also offers to jump to a page by entering its number.
 */
class PageJumpNavigationToolbar(table: DataTable<*, *>) : BootstrapNavigationToolbar(table) {

    override fun newPagingNavigator(
        navigatorId: String,
        table: DataTable<*, *>,
        size: BootstrapPagingNavigator.Size,
    ): BootstrapPagingNavigator = PageJumpPagingNavigator(navigatorId, table).apply { setSize(size) }

    companion object {
        private const val serialVersionUID = 1L
    }
}
