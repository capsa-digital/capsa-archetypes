package digital.capsa.query.controller

import digital.capsa.query.services.BookService
import digital.capsa.query.services.LibraryService
import digital.capsa.query.services.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/library")
class WebController {

    @Autowired
    lateinit var libraryService: LibraryService

    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var memberService: MemberService

    @GetMapping(value = ["/home"])
    fun showAllBooks(model: Model): String {
        model.addAttribute("libraries", libraryService.getLibraryList())
        model.addAttribute("members", memberService.getMemberList())
        return "homePage"
    }
}