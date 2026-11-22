package ch.difty.scipamato.common

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

private const val PROPERTY_KEY = "propertyKey"

internal class UtilsTest {

    //region:asProperty

    private val values = listOf(PropertyTestEnum.VAL1, PropertyTestEnum.VAL2, PropertyTestEnum.DEFAULT)

    private enum class PropertyTestEnum { VAL1, VAL2, DEFAULT }

    @Test
    fun asProperty_witValues_returnsValue() {
        "VAL2".asProperty(values, PropertyTestEnum.DEFAULT, PROPERTY_KEY) shouldBeEqualTo PropertyTestEnum.VAL2
    }

    @Test
    fun asProperty_withoutValues_returnsDefault() {
        "VAL2".asProperty(emptyList(), PropertyTestEnum.DEFAULT, PROPERTY_KEY) shouldBeEqualTo PropertyTestEnum.DEFAULT
    }

    @Test
    fun asProperty_withBlankReceiver_returnsDefault() {
        "".asProperty(emptyList(), PropertyTestEnum.DEFAULT, PROPERTY_KEY) shouldBeEqualTo PropertyTestEnum.DEFAULT
    }

    //endregion
}
