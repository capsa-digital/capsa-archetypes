package digital.capsa.it

import digital.capsa.it.json.JsonPathModifyer
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit")
class JsonPathModifierTest {

    @Test
    @Suppress("FunctionNaming")
    fun testModifier_happyPath() {
        Assert.assertEquals(
                """[{"id":"12345","data":"qwert","num":12345},{"id":"23456","data":"asdfg","num":23456}]""",
                JsonPathModifyer.modifyJson("""
            [{
              "id": "12345",
              "data": "abcd",
              "num": 12345
            }, {
              "id": "23456",
              "data": "bcde",
              "num": 23456
            }]
        """.trimIndent(), mapOf("@[?(@.id == '12345')].data" to "qwert",
                        "@[?(@.id == '23456')].data" to "asdfg")
                ))
    }

    @Test
    @Suppress("FunctionNaming")
    fun testModifier_date() {
        Assert.assertEquals("""[{"id":"12345","endDate":[2019,7,30,23,59]}]""",
                JsonPathModifyer.modifyJson("""
            [{
              "id": "12345",
              "endDate": [
                  2019,
                  7,
                  31,
                  23,
                  59
                ]
            }]

        """.trimIndent(), mapOf("@[?(@.id == '12345')].endDate" to arrayOf(2019, 7, 30, 23, 59))
                ))
    }

    @Test
    @Suppress("FunctionNaming")
    fun testModifyer_shift_start_date() {
        Assert.assertThat(
                JsonPathModifyer.modifyJson("""
                    [{
                      "id": "12345",
                      "endDate": [
                          2019,
                          7,
                          31,
                          23,
                          59
                        ]
                    }]
        """.trimIndent(), mapOf("@[?(@.id == '12345')].endDate" to "{shiftStartDateHours(-5)}")
                ), Matchers.matchesPattern("""\[\{"id":"12345","endDate":\[.*\]\}\]"""))
    }

    @Test
    @Suppress("FunctionNaming")
    fun testModifyer_shift_end_date_collection() {
        Assert.assertThat(
                JsonPathModifyer.modifyJson("""
                    [{
                      "id": "12345",
                      "endDate": [
                          2019,
                          7,
                          31,
                          23,
                          59
                        ]
                    }]
        """.trimIndent(), mapOf("@[?(@.id == '12345')].endDate" to "{shiftEndDateHours(5)}")
                ), Matchers.matchesPattern("""\[\{"id":"12345","endDate":\[.*\]\}\]"""))
    }

    @Test
    @Suppress("FunctionNaming")
    fun testModifyer_shift_end_date() {
        Assert.assertThat(
                JsonPathModifyer.modifyJson("""
                    {
                      "data": {
                          "endDate": [
                              2019,
                              7,
                              31,
                              23,
                              59
                            ]
                        }
                    }
        """.trimIndent(), mapOf("@.data.endDate" to "{shiftEndDateHours(5)}")
                ), Matchers.matchesPattern("""\{"data":\{"endDate":\[.*\]\}\}"""))
    }
}
