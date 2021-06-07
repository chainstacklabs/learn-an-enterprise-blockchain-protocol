package com.helloBlock.contracts;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.TypeOnlyCommandData;
import net.corda.core.transactions.LedgerTransaction;
import com.helloBlock.states.helloBlockState;
import org.jetbrains.annotations.NotNull;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

// ************
// * Contract *
// ************
// Contract and state.
public class helloBlockContract implements Contract {
    // Used to identify our contract when building a transaction.
    public static final String ID = "com.helloBlock.contracts.helloBlockContract";

    // Contract code.
    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        final CommandWithParties<Commands.Send> command = requireSingleCommand(tx.getCommands(), Commands.Send.class);
        requireThat(require -> {
        final helloBlockState helloBlock = tx.outputsOfType(helloBlockState.class).get(0);
        require.using("Sender must sign the message.", helloBlock.getOrigin().getOwningKey().equals(command.getSigners().get(0)));
        return null;
        });
    }

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {
        class Send extends TypeOnlyCommandData {}
    }
}