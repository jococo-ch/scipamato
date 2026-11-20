package ch.difty.scipamato.core.web.user

import ch.difty.scipamato.core.web.WicketTest
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldHaveSize
import org.apache.wicket.core.util.lang.WicketObjects
import org.apache.wicket.validation.Validatable
import org.junit.jupiter.api.Test

internal class CurrentPasswordMatchesValidatorTest : WicketTest() {

    private val v by lazy { CurrentPasswordMatchesValidator(PW_ADMIN_ENCRYPTED) }

    private lateinit var validatable: Validatable<String>

    @Test
    fun withMatchingHash_succeeds() {
        validatable = Validatable(PW_ADMIN)
        v.validate(validatable)
        validatable.isValid.shouldBeTrue()
        validatable.errors.shouldBeEmpty()
    }

    @Test
    fun withNonMatchingHash_succeeds() {
        validatable = Validatable(PW_ADMIN + "X")
        v.validate(validatable)
        validatable.isValid.shouldBeFalse()
        validatable.errors shouldHaveSize 1
    }

    @Test
    fun afterSerializationRoundTrip_stillValidates() {
        val clone = WicketObjects.cloneObject(v)
        validatable = Validatable(PW_ADMIN)
        clone.validate(validatable)
        validatable.isValid.shouldBeTrue()
    }

    companion object {
        private const val PW_ADMIN = "admin"
        private const val PW_ADMIN_ENCRYPTED = "$2a$04\$oOL75tgCf3kXdr6vO5gagu6sIUZWfXyEhZHmDd4LpGvOPTaO5xEoO"
    }
}
