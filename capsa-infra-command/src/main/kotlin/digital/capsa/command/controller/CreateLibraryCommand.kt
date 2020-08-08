package digital.capsa.command.controller

import digital.capsa.core.model.Address

data class CreateLibraryCommand(

        var libraryName: String,

        var address: Address
)

