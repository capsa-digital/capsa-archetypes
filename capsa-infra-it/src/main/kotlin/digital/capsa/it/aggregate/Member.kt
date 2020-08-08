package digital.capsa.it.aggregate

import digital.capsa.core.logger
import java.util.Random

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
        val domain = ((parent as Library).parent as Account).domain
        firstName = firstName
                ?: PersonMockGenerator.mockFirstName(index = index, gender = gender)
        lastName = lastName
                ?: PersonMockGenerator.mockLastName(index = index)
        email = email
                ?: PersonMockGenerator.mockEmail(index = index, firstName = firstName!!, lastName = lastName, domain = domain!!)
        phone = phone
                ?: PersonMockGenerator.mockPhone(random = random)
    }

    override fun onCreate(context: AggregateBuilderContext) {
        logger.info("===> Member added, attr = ${getAttributes()}")
    }

    companion object {
        val random = Random(0)
    }
}