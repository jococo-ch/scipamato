package ch.difty.scipamato.common.web.component.table

import org.apache.wicket.Component
import org.apache.wicket.application.IComponentInstantiationListener
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink
import org.apache.wicket.markup.ComponentTag

/**
 * Replaces the inline style `text-decoration: none;` that wicket-bootstrap's
 * `BootstrapOrderByBorder` markup puts on its sort links with the equivalent
 * Bootstrap utility class. A strict Content Security Policy blocks inline
 * style attributes.
 *
 * Register with the application's component instantiation listeners.
 */
class OrderByLinkStyleReplacer : IComponentInstantiationListener {
    override fun onInstantiation(component: Component) {
        if (component is OrderByLink<*>) component.add(TextDecorationNoneStyleToClass())
    }
}

private class TextDecorationNoneStyleToClass : Behavior() {
    override fun onComponentTag(component: Component, tag: ComponentTag) {
        if (tag.getAttribute(STYLE) == TEXT_DECORATION_NONE) {
            tag.remove(STYLE)
            tag.append("class", "text-decoration-none", " ")
        }
    }

    companion object {
        private const val serialVersionUID = 1L
        private const val STYLE = "style"
        private const val TEXT_DECORATION_NONE = "text-decoration: none;"
    }
}
