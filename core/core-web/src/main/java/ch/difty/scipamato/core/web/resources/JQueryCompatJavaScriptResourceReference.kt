package ch.difty.scipamato.core.web.resources

import org.apache.wicket.Application
import org.apache.wicket.markup.head.HeaderItem
import org.apache.wicket.markup.head.JavaScriptHeaderItem
import org.apache.wicket.request.resource.JavaScriptResourceReference

/**
 * Restores the jQuery utility functions that jQuery 4 removed and that the
 * datepicker and file input of wicket-bootstrap-extensions still call.
 */
object JQueryCompatJavaScriptResourceReference :
    JavaScriptResourceReference(JQueryCompatJavaScriptResourceReference::class.java, "js/jquery-compat.js") {
    fun readResolve(): Any = JQueryCompatJavaScriptResourceReference
    private const val serialVersionUID: Long = 1L

    override fun getDependencies(): List<HeaderItem> =
        listOf(JavaScriptHeaderItem.forReference(Application.get().javaScriptLibrarySettings.jQueryReference))
}
