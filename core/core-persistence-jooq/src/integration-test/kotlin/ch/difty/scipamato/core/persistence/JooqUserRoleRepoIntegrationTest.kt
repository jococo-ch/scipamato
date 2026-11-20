package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.persistence.JooqIntegrationTest
import ch.difty.scipamato.core.auth.Role
import ch.difty.scipamato.core.persistence.user.JooqUserRoleRepo
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@Suppress("FunctionName", "MagicNumber")
@JooqIntegrationTest
@Import(JooqUserRoleRepoIntegrationTest.CachingConfig::class)
internal open class JooqUserRoleRepoIntegrationTest {

    /**
     * Enables caching so the tests also cover the interplay of
     * `@Cacheable` and `@CacheEvict` in [JooqUserRoleRepo].
     *
     * Intentionally not annotated with `@TestConfiguration`/`@Configuration`:
     * the component scan of [TestApplication] would otherwise pick it up
     * and enable caching in all other integration tests as well.
     */
    @EnableCaching(proxyTargetClass = true)
    open class CachingConfig {
        @Bean
        open fun cacheManager(): CacheManager = ConcurrentMapCacheManager("userRolesByUserId")
    }

    @Autowired
    private val repo: JooqUserRoleRepo? = null

    @Autowired
    private val cacheManager: CacheManager? = null

    /** The DB changes are rolled back after each test, the cache entries must be cleared as well. */
    @AfterEach
    fun clearCache() {
        cacheManager!!.getCache("userRolesByUserId")!!.clear()
    }

    @Test
    fun findingRolesForUser_withTwoRoles_returnsBoth() {
        val roles = repo!!.findRolesForUser(4)
        roles shouldContainSame listOf(Role.ADMIN, Role.USER)
    }

    @Test
    fun findingRolesForUser_withNoRoles_returnsNone() {
        val roles = repo!!.findRolesForUser(7)
        roles.shouldBeEmpty()
    }

    @Test
    fun addingNewUserRoles_toUserWithCachedRoles_evictsCache() {
        // populate the cache with the current roles
        repo!!.findRolesForUser(7).shouldBeEmpty()

        repo.addNewUserRolesOutOf(7, setOf(Role.VIEWER))

        // the stale cache entry must have been evicted
        repo.findRolesForUser(7) shouldContainSame listOf(Role.VIEWER)
    }

    @Test
    fun addingNewUserRoles_withExistingAndNewRoles_ignoresDuplicatesAndKeepsExisting() {
        repo!!.addNewUserRolesOutOf(4, setOf(Role.ADMIN, Role.VIEWER))

        repo.findRolesForUser(4) shouldContainSame listOf(Role.ADMIN, Role.USER, Role.VIEWER)
    }
}
