package ch.difty.scipamato.core.web.behavior

import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior

class SortableBehavior(private val orderField: Component) : Behavior() {
    companion object {
        private const val serialVersionUID = 1L
    }
}
