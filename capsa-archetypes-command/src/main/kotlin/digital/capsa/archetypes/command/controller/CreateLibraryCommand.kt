package digital.capsa.archetypes.command.controller

import digital.capsa.archetypes.core.model.Address

data class CreateLibraryCommand(

        var libraryName: String,

        var address: Address
)

