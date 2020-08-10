package digital.capsa.it.dsl

import assertk.assertAll

typealias given<S> = Given<S>

class Given<S>(private val setup: () -> S) {

    fun <R> on(test: S.() -> R): Result<R> =
            Result { setup().test() }
}

class Result<R>(private val result: () -> R) {

    fun then(assert: Assertions.(R) -> Unit) {
        val assertions = Assertions()
        assertAll {
            assertions.assert(result())
        }
    }
}

class Assertions
