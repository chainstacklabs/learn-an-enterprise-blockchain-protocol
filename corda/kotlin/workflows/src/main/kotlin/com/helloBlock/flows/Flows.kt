package com.helloBlock.flows

import com.helloBlock.contracts.helloBlockContract
import com.helloBlock.states.helloBlockState
import co.paralleluniverse.fibers.Suspendable
import net.corda.core.contracts.Command
import net.corda.core.contracts.StateAndContract
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker

// *********
// * Flows *
// *********
@InitiatingFlow
@StartableByRPC
class helloBlockFlow(val target: Party) : FlowLogic<SignedTransaction>() {

    override val progressTracker: ProgressTracker = tracker()

    companion object {
        object GENERATING_TRANSACTION : ProgressTracker.Step("Generating a 'Hello, Block! transaction'")
        object SIGNING_TRANSACTION : ProgressTracker.Step("Signing the 'Hello, Block! transaction'")
        object VERIFYING_TRANSACTION : ProgressTracker.Step("Verifying the 'Hello, Block! transaction'")
        object FINALIZING_TRANSACTION : ProgressTracker.Step("Finalizing the 'Hello, Block! transaction'") {
            override fun childProgressTracker() = FinalityFlow.tracker()
        }

        fun tracker() = ProgressTracker(
                GENERATING_TRANSACTION,
                VERIFYING_TRANSACTION,
                SIGNING_TRANSACTION,
                FINALIZING_TRANSACTION
        )
    }

    @Suspendable
    override fun call(): SignedTransaction {
        progressTracker.currentStep = GENERATING_TRANSACTION

        val me = serviceHub.myInfo.legalIdentities.first()
        val notary = serviceHub.networkMapCache.notaryIdentities.single()
        val command = Command(helloBlockContract.Commands.Send(), listOf(me.owningKey))
        val state = helloBlockState(me, target)
        val stateAndContract = StateAndContract(state, helloBlockContract.ID)
        val utx = TransactionBuilder(notary = notary).withItems(stateAndContract, command)

        progressTracker.currentStep = SIGNING_TRANSACTION
        val stx = serviceHub.signInitialTransaction(utx)

        progressTracker.currentStep = VERIFYING_TRANSACTION
        stx.verify(serviceHub)

        progressTracker.currentStep = FINALIZING_TRANSACTION
        val targetSession = initiateFlow(target)
        return subFlow(FinalityFlow(stx, listOf(targetSession), FINALIZING_TRANSACTION.childProgressTracker()))
    }
}

@InitiatedBy(helloBlockFlow::class)
class helloBlockFlowResponder(val counterpartySession: FlowSession) : FlowLogic<SignedTransaction>() {
    @Suspendable
    override fun call(): SignedTransaction {
        return subFlow(ReceiveFinalityFlow(counterpartySession))
    }
}