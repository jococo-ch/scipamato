package ch.difty.scipamato.core.web.behavior

import de.agilecoders.wicket.extensions.markup.html.bootstrap.jqueryui.JQueryUIJavaScriptReference
import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.JavaScriptHeaderItem
import org.apache.wicket.markup.head.OnDomReadyHeaderItem

/**
 * Makes the children of the component sortable by drag and drop (jQuery UI sortable).
 * After every move, the `data-id` attributes of the children, in their new order and
 * separated by commas, are written into [orderField].
 */
class SortableBehavior(private val orderField: Component) : Behavior() {

    override fun bind(component: Component) {
        super.bind(component)
        component.outputMarkupId = true
        orderField.outputMarkupId = true
    }

    override fun renderHead(component: Component, response: IHeaderResponse) {
        super.renderHead(component, response)
        response.render(JavaScriptHeaderItem.forReference(JQueryUIJavaScriptReference.instance()))
        response.render(
            OnDomReadyHeaderItem.forScript(
                "$('#${component.markupId}').sortable({ update: function () { " +
                    "$('#${orderField.markupId}').val(" +
                    "$(this).sortable('toArray', { attribute: 'data-id' }).join(',')); } });"
            )
        )
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
