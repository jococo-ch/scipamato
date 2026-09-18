package ch.difty.scipamato

import org.apache.wicket.protocol.http.FetchMetadataResourceIsolationPolicy.SAME_ORIGIN
import org.apache.wicket.protocol.http.FetchMetadataResourceIsolationPolicy.SEC_FETCH_SITE_HEADER
import org.apache.wicket.util.tester.FormTester
import org.apache.wicket.util.tester.WicketTester

fun WicketTester.clickLinkSameOrigin(path: String) {
    addRequestHeader(SEC_FETCH_SITE_HEADER, SAME_ORIGIN)
    clickLink(path)
}

fun WicketTester.newFormTesterSameOrigin(path: String, fillBlankString: Boolean = true): FormTester {
    addRequestHeader(SEC_FETCH_SITE_HEADER, SAME_ORIGIN)
    return newFormTester(path, fillBlankString)
}

fun WicketTester.submitFormSameOrigin(path: String): Unit {
    addRequestHeader(SEC_FETCH_SITE_HEADER, SAME_ORIGIN)
    submitForm(path)
}
