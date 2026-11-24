package ch.difty.scipamato.core

import org.apache.wicket.csp.ContentSecurityPolicySettings

/**
 * Configures the Content Security Policy of SciPaMaTo-Core.
 */
fun ContentSecurityPolicySettings.configureCoreContentSecurityPolicy() {
    reporting().strict().reportBack()
}
