package digital.capsa.query.events

import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.core.logger
import digital.capsa.eventbus.Event
import digital.capsa.eventbus.EventBusInput
import digital.capsa.eventbus.data.MemberRegistered
import digital.capsa.query.model.member.Member
import digital.capsa.query.services.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener

@EnableBinding(EventBusInput::class)
class MemberRegisteredEventHandler(private var memberService: MemberService) {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @StreamListener(EventBusInput.INPUT, condition = "headers['eventType']=='MemberRegistered'")
    fun handleEvent(event: Event<MemberRegistered>) {
        logger.info("\n<==== ${objectMapper.writeValueAsString(event)}")
        memberService.registerMember(event.data.transform())
    }

    fun MemberRegistered.transform(): Member = Member(
            memberId = memberId,
            firstName = firstName,
            lastName = lastName,
            email = email,
            phone = phone
    )
}
