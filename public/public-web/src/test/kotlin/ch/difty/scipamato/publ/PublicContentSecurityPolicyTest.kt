package ch.difty.scipamato.publ

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.apache.wicket.mock.MockApplication
import org.apache.wicket.util.tester.DummyHomePage
import org.apache.wicket.util.tester.WicketTester

internal class PublicContentSecurityPolicyTest : FunSpec({

    lateinit var tester: WicketTester

    beforeTest {
        tester = WicketTester(object : MockApplication() {
            override fun init() {
                // WebApplication defaults to a strict blocking policy; MockApplication disables it.
                cspSettings.blocking().strict()
                cspSettings.configurePublicContentSecurityPolicy()
                super.init()
            }
        })
        tester.startPage(DummyHomePage::class.java)
    }

    afterTest { tester.destroy() }

    test("pages carry the strict policy in blocking mode") {
        val policy = tester.lastResponse.getHeader("Content-Security-Policy")

        policy.shouldNotBeNull()
        policy shouldContain "default-src 'none'"
        policy shouldContain "script-src 'strict-dynamic' 'nonce-"
        policy shouldContain "style-src 'nonce-"
    }

    test("the policy allows data: images, which Bootstrap's navbar toggler icon uses") {
        tester.lastResponse.getHeader("Content-Security-Policy")
            .shouldNotBeNull() shouldContain "img-src 'self' data:;"
    }

    test("the blocking policy does not report back to the application") {
        tester.lastResponse.getHeader("Content-Security-Policy").shouldNotBeNull() shouldNotContain "report-uri"
    }

    test("pages carry no report-only policy") {
        tester.lastResponse.getHeader("Content-Security-Policy-Report-Only").shouldBeNull()
    }
})
