package digital.capsa.it.aggregate

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.core.vocab.AggregateType
import java.util.Random
import java.util.UUID

class Member(
        var firstName: String? = null,
        var lastName: String? = null,
        var email: String? = null,
        var phone: String? = null
) : AbstractAggregate("Member") {

    override fun construct() {
        val index = parent?.getChildCount(Member::class) ?: 0
        val nextInt = random.nextInt(4)
        val gender = PersonMockGenerator.Gender.values()[if (nextInt != 0) 0 else 1]
        firstName = firstName
                ?: PersonMockGenerator.mockFirstName(index = index, gender = gender)
        lastName = lastName
                ?: PersonMockGenerator.mockLastName(index = index)
        email = email
                ?: PersonMockGenerator.mockEmail(index = index, firstName = firstName!!, lastName = lastName)
        phone = phone
                ?: PersonMockGenerator.mockPhone(random = random)
    }

    override fun onCreate() {
        httpRequest("/requests/register-member.json")
                .withTransformation(
                        "$.schema" to context.environment.getProperty("capsa.schema"),
                        "$.host" to context.environment.getProperty("capsa.host"),
                        "$.port" to context.environment.getProperty("capsa.port"),
                        "$.body.firstName" to firstName,
                        "$.body.lastName" to lastName,
                        "$.body.email" to email,
                        "$.body.phone" to phone
                )
                .send {
                    assertThat(statusCode.value()).isEqualTo(200)
                    val ids = ObjectMapper().readTree(body)?.get("ids")
                    ids?.also {
                        it.get(AggregateType.member.name)?.also { node ->
                            id = UUID.fromString(node.asText())
                        }
                    }
                }
    }

    companion object {
        val random = Random(0)
    }
}