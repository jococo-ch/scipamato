package ch.difty.scipamato.publ

import ch.difty.scipamato.publ.web.WicketTest
import ch.difty.scipamato.publ.web.authentication.LoginPage
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

class ScipamatoPublicApplicationCspTest : WicketTest() {

    @Test
    fun applicationPages_carryTheReportOnlyPolicy() {
        tester.startPage(LoginPage::class.java)

        val policy = tester.lastResponse.getHeader("Content-Security-Policy-Report-Only")
        policy.shouldNotBeNull()
        policy shouldContain "default-src 'none'"
    }
}
