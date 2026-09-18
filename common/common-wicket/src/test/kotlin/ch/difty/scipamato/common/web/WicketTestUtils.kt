package ch.difty.scipamato.common.web

import org.apache.wicket.protocol.http.FetchMetadataResourceIsolationPolicy.SAME_ORIGIN
import org.apache.wicket.protocol.http.FetchMetadataResourceIsolationPolicy.SEC_FETCH_SITE_HEADER
import org.apache.wicket.util.tester.FormTester
import org.apache.wicket.util.tester.WicketTester

fun WicketTester.submitFormSameOrigin(path: String) {
    addRequestHeader(SEC_FETCH_SITE_HEADER, SAME_ORIGIN)
    submitForm(path)
}

fun WicketTester.clickLinkSameOrigin(path: String) {
    addRequestHeader(SEC_FETCH_SITE_HEADER, SAME_ORIGIN)
    clickLink(path)
}

fun WicketTester.newFormTesterSameOrigin(path: String): FormTester {
    addRequestHeader(SEC_FETCH_SITE_HEADER, SAME_ORIGIN)
    return newFormTester(path)
}
