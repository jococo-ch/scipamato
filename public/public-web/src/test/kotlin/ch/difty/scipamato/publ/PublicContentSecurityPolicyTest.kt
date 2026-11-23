package ch.difty.scipamato.publ

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContain
import org.apache.wicket.mock.MockApplication
import org.apache.wicket.util.tester.DummyHomePage
import org.apache.wicket.util.tester.WicketTester

internal class PublicContentSecurityPolicyTest : FunSpec({

    lateinit var tester: WicketTester

    beforeTest {
        tester = WicketTester(object : MockApplication() {
            override fun init() {
                cspSettings.configurePublicContentSecurityPolicy()
                super.init()
            }
        })
        tester.startPage(DummyHomePage::class.java)
    }

    afterTest { tester.destroy() }

    test("pages carry the strict policy in report-only mode") {
        val policy = tester.lastResponse.getHeader("Content-Security-Policy-Report-Only")

        policy.shouldNotBeNull()
        policy shouldContain "default-src 'none'"
        policy shouldContain "script-src 'strict-dynamic' 'nonce-"
        policy shouldContain "report-uri"
    }
})
