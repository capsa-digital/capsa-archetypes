package digital.capsa.it.aggregate

import java.util.Random

object PersonMockGenerator {

    enum class Gender {
        m, f
    }

    private val firstNamesMale: List<String> = listOf("Michael", "James", "Joshua", "Daniel", "David", "Matthew",
            "Christopher", "Joseph", "Jason", "Robert", "John", "Andrew", "Justin", "Eric", "Ryan",
            "Kevin", "Andrew", "William", "Patrick", "Jesse")
    private val firstNamesFemale: List<String> = listOf("Florence", "Katy", "Rachel", "Rhonda", "Caitlyn", "Nella",
            "Ellie-Mae", "Isobelle", "Annabelle", "Charlotte", "Lana", "Alannah", "Lauren", "Joel", "Tia",
            "Genevieve", "Angie", "Aminah", "Cassie", "Cerys")
    private val lastNames: List<String> = listOf("Mccarthy", "Crawford", "O'Brien", "Hunt", "Burke", "Berry",
            "Holmes", "Edwards", "Mcbride", "Snyder", "Perez", "Fleming", "Abbott", "Kelley", "Elliott",
            "Walton", "Todd", "Hubbard", "Rivera", "Johnston")

    fun mockFirstName(index: Int, gender: Gender): String {
        return when (gender) {
            Gender.m -> firstNamesMale[index % firstNamesMale.size]
            Gender.f -> firstNamesFemale[index % firstNamesFemale.size]
        }
    }

    fun mockLastName(index: Int): String {
        return lastNames[index % lastNames.size]
    }

    fun mockEmail(index: Int, firstName: String, lastName: String?): String {
        val firstNameSegment = firstName.replace("\\s".toRegex(), "")
        val lastNameSegment = lastName?.let { "." + it.replace("\\s".toRegex(), "") } ?: ""
        return "$firstNameSegment$lastNameSegment@bookclub$index.ca"
    }

    fun mockPhone(random: Random): String? {
        val num1 = random.nextInt(7) + 1
        val num2 = random.nextInt(8)
        val num3 = random.nextInt(8)
        val set2 = random.nextInt(643) + 100
        val set3 = random.nextInt(8999) + 1000
        return "($num1$num2$num3)-$set2-$set3"
    }

    private val random = Random(10)
}