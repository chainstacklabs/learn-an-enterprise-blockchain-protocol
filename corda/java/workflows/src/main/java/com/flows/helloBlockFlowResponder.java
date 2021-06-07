package com.helloBlock.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

@InitiatedBy(helloBlockFlow.class)
public class helloBlockFlowResponder extends FlowLogic<SignedTransaction> {
    private FlowSession counterpartySession;

    helloBlockFlowResponder(FlowSession counterpartySession){
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        return subFlow(new ReceiveFinalityFlow(this.counterpartySession));
    }
}



