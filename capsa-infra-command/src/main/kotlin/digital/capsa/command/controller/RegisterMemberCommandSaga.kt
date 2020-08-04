package digital.capsa.command.controller

import digital.capsa.core.vocab.AggregateType
import digital.capsa.eventbus.SagaManager
import digital.capsa.eventbus.data.MemberCreated
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
        val memberId: UUID = UUID.randomUUID()
        val sagaId = sagaManager.runSaga(
                memberCreated(memberId = memberId)
        )
        return CommandResponse(
                saga = sagaId,
                ids = mapOf(
                        AggregateType.member to memberId
                )
        )
    }

    private fun RegisterMemberCommand.memberCreated(memberId: UUID) = MemberCreated(
            memberId = memberId,
            name = name,
            email = email
    )
}
