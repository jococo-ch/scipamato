package ch.difty.scipamato.publ

import org.apache.wicket.csp.CSPDirective
import org.apache.wicket.csp.CSPDirectiveSrcValue
import org.apache.wicket.csp.ContentSecurityPolicySettings

/**
 * Configures the Content Security Policy of SciPaMaTo-Public.
 */
fun ContentSecurityPolicySettings.configurePublicContentSecurityPolicy() {
    blocking().strict()
        .add(CSPDirective.STYLE_SRC, CSPDirectiveSrcValue.SELF)
        .add(CSPDirective.IMG_SRC, "data:")
}
