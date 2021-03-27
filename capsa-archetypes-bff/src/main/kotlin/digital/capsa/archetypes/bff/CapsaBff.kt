package digital.capsa.archetypes.bff

import io.ktor.application.call
import io.ktor.http.content.defaultResource
import io.ktor.http.content.static
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8000) {
        routing {
            static("/") {
                defaultResource("index.html")
            }
            get("/bff") {
                call.respondText("Hello, bff!")
            }
        }
    }.start(wait = true)
}
