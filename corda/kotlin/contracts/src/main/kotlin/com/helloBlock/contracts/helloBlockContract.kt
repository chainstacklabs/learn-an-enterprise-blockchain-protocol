package com.helloBlock.contracts

import net.corda.core.contracts.*
import net.corda.core.contracts.Requirements.using
import net.corda.core.transactions.LedgerTransaction
import com.helloBlock.states.helloBlockState

// ************
// * Contract *
// ************
// Contract and state.
class helloBlockContract: Contract {
    companion object {
        // Used to identify our contract when building a transaction.
        const val ID = "com.helloBlock.contracts.helloBlockContract"
    }

    // Contract code.
    override fun verify(tx: LedgerTransaction) {
        val command = tx.commands.requireSingleCommand<Commands.Send>()
        val helloBlock = tx.outputsOfType<helloBlockState>().single()
        "Sender must sign the message." using (helloBlock.origin.owningKey == command.signers.single())
    }

    // Used to indicate the transaction's intent.
    interface Commands : CommandData {
        class Send : Commands
    }
}