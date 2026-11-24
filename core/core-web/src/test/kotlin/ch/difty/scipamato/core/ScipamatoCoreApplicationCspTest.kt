package ch.difty.scipamato.core

import ch.difty.scipamato.core.web.AbstractWicketTest
import ch.difty.scipamato.core.web.authentication.LoginPage
import ch.difty.scipamato.core.web.paper.list.PaperListPage
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotBeNull
import org.apache.wicket.Page
import org.apache.wicket.util.tester.WicketTester
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

internal class ScipamatoCoreApplicationCspTest : AbstractWicketTest() {

    @Autowired
    private lateinit var context: ApplicationContext

    private lateinit var tester: WicketTester

    @BeforeEach
    fun setUp() {
        // The test context holds several pages annotated as home page.
        val coreApplication = object : ScipamatoCoreApplication() {
            override fun getHomePage(): Class<out Page> = PaperListPage::class.java
        }
        context.autowireCapableBeanFactory.autowireBean(coreApplication)
        tester = WicketTester(coreApplication)
    }

    @AfterEach
    fun tearDown() {
        tester.destroy()
    }

    @Test
    fun applicationPages_carryTheReportOnlyPolicy() {
        tester.startPage(LoginPage::class.java)

        val policy = tester.lastResponse.getHeader("Content-Security-Policy-Report-Only")
        policy.shouldNotBeNull()
        policy shouldContain "default-src 'none'"
    }
}
