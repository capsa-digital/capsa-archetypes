package digital.capsa.archetypes.query.events

import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.core.logger
import digital.capsa.archetypes.eventbus.Event
import digital.capsa.archetypes.eventbus.EventBusInput
import digital.capsa.archetypes.eventbus.data.MemberRegistered
import digital.capsa.archetypes.query.model.member.Member
import digital.capsa.archetypes.query.services.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener

@EnableBinding(EventBusInput::class)
open class MemberRegisteredEventHandler(private var memberService: MemberService) {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @StreamListener(EventBusInput.INPUT, condition = "headers['eventType']=='MemberRegistered'")
    fun handleEvent(event: Event<MemberRegistered>) {
        logger.info("\n<==== ${objectMapper.writeValueAsString(event)}")
        memberService.registerMember(event.data.transform())
    }

    private fun MemberRegistered.transform(): Member = Member(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone
    )
}
