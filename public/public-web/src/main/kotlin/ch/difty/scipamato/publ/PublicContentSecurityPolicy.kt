package ch.difty.scipamato.publ

import org.apache.wicket.csp.ContentSecurityPolicySettings

/**
 * Configures the Content Security Policy of SciPaMaTo-Public.
 */
fun ContentSecurityPolicySettings.configurePublicContentSecurityPolicy() {
    reporting().strict().reportBack()
}
