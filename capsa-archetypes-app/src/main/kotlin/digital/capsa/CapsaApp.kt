package digital.capsa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CapsaApp

fun main(args: Array<String>) {
    runApplication<CapsaApp>(*args)
}
