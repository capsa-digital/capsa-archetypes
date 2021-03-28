import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.dom.h3

@JsExport
class App : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        div {
            h3 {
                +"Capsa WEB"
            }
        }
    }
}
