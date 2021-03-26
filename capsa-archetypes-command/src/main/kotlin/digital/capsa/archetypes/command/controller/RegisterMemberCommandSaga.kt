package digital.capsa.archetypes.command.controller

import digital.capsa.archetypes.core.aggregates.MemberId
import digital.capsa.archetypes.eventbus.SagaManager
import digital.capsa.archetypes.eventbus.data.MemberRegistered
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.function.Function

@Service
class RegisterMemberCommandSaga(private val sagaManager: SagaManager) {

    @Bean
    @Qualifier("registerMember")
    fun registerMember(): Function<RegisterMemberCommand, CommandResponse> {
        return Function { command ->
            command.registerMemberSaga()
        }
    }

    private fun RegisterMemberCommand.registerMemberSaga(): CommandResponse {
        val memberId = MemberId(UUID.randomUUID())
        val sagaId = sagaManager.runSaga(
            memberRegistered(memberId = memberId)
        )
        return CommandResponse(
            saga = sagaId,
            ids = listOf(memberId)
        )
    }

    private fun RegisterMemberCommand.memberRegistered(memberId: MemberId) = MemberRegistered(
        id = memberId,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone
    )
}
