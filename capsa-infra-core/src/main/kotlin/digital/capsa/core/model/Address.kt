package com.metrofoxsecurity.core.model

import javax.persistence.Embeddable
import javax.validation.constraints.Size

@Embeddable
data class Address(
        @Size(max = 100)
        var addressLine1: String? = null,

        @Size(max = 100)
        var addressLine2: String? = null,

        @Size(max = 100)
        var city: String? = null,

        @Size(max = 100)
        var state: String? = null,

        @Size(max = 100)
        var country: String? = null,

        @Size(max = 6)
        var zipCode: String? = null
) {
    override fun toString(): String {
        return "$addressLine1, $city, $state $zipCode"
    }
}
