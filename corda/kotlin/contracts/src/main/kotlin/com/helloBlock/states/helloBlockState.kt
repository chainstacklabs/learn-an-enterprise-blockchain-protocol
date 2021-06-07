package com.helloBlock.states

import com.helloBlock.contracts.helloBlockContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.ContractState
import net.corda.core.identity.Party

// *********
// * State *
// *********
@BelongsToContract(helloBlockContract::class)
data class helloBlockState(val origin: Party,
                   val target: Party,
                   val helloBlock: String = "Hello, Block!") : ContractState {
    override val participants get() = listOf(target)
}
