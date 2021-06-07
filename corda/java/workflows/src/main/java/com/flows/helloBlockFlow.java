package com.helloBlock.flows;

import com.helloBlock.contracts.helloBlockContract;
import com.helloBlock.states.helloBlockState;
import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.StateAndContract;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.node.ServiceHub;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import net.corda.core.utilities.ProgressTracker.Step;

import java.security.SignatureException;

// *********
// * Flows *
// *********
@InitiatingFlow
@StartableByRPC
public class helloBlockFlow extends FlowLogic<SignedTransaction> {
    private final Step GENERATING_TRANSACTION = new Step("Generating a 'Hello, Block! transaction");
    private final Step SIGNING_TRANSACTION = new Step("Signing the 'Hello, Block! transaction");
    private final Step VERIFYING_TRANSACTION = new Step("Verifying the 'Hello, Block! transaction");
    private final Step FINALIZING_TRANSACTION = new Step("Finalizing the 'Hello, Block! transaction") {
        @Override
        public ProgressTracker childProgressTracker() {
            return FinalityFlow.Companion.tracker();
        }
    };
    private final Party target;
    private final ProgressTracker progressTracker = new ProgressTracker(
            GENERATING_TRANSACTION,
            SIGNING_TRANSACTION,
            VERIFYING_TRANSACTION,
            FINALIZING_TRANSACTION
    );

    public helloBlockFlow(Party target){
        this.target = target;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {

        progressTracker.setCurrentStep(GENERATING_TRANSACTION);
        ServiceHub serviceHub = getServiceHub();
        Party me = serviceHub.getMyInfo().getLegalIdentities().get(0);
        Party notary = serviceHub.getNetworkMapCache().getNotaryIdentities().get(0);
        Command command = new Command<>( new helloBlockContract.Commands.Send(),
                ImmutableList.of(me.getOwningKey()));
        helloBlockState state = new helloBlockState(me,target,"helloBlock");
        StateAndContract stateAndContract = new StateAndContract(state, helloBlockContract.ID);
        TransactionBuilder utx = new TransactionBuilder(notary).withItems(stateAndContract,command);

        progressTracker.setCurrentStep(SIGNING_TRANSACTION);
        SignedTransaction stx = serviceHub.signInitialTransaction(utx);

        progressTracker.setCurrentStep(VERIFYING_TRANSACTION);
        try {
            stx.verify(serviceHub);
        } catch (SignatureException e) {
            throw new FlowException();
        }

        progressTracker.setCurrentStep(FINALIZING_TRANSACTION);
        FlowSession targetSession = initiateFlow(target);
        return subFlow(new FinalityFlow(stx, ImmutableSet.of(targetSession), FINALIZING_TRANSACTION.childProgressTracker()));

    }
}


