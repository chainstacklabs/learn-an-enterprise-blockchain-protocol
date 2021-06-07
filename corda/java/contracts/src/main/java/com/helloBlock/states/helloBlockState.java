package com.helloBlock.states;

import com.helloBlock.contracts.helloBlockContract;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.serialization.ConstructorForDeserialization;
import net.corda.core.serialization.CordaSerializable;
import java.util.List;

@CordaSerializable
@BelongsToContract(helloBlockContract.class)
public class helloBlockState implements ContractState {
    public AbstractParty origin;
    public AbstractParty target;
    public String helloBlock;

    public AbstractParty getOrigin() {
        return origin;
    }

    public AbstractParty getTarget() {
        return target;
    }

    public String getHelloBlock() { return helloBlock; }

    @ConstructorForDeserialization
    public helloBlockState(AbstractParty origin, AbstractParty target, String helloBlock){
        this.origin = origin;
        this.target = target;
        this.helloBlock = helloBlock;
    }

    public helloBlockState(AbstractParty origin, AbstractParty target){
        this(origin,target,"Hello, Block!");
    }

    @Override
    public String toString(){
        return origin.nameOrNull() + "helloBlock";
    }

    @Override
    public List<AbstractParty> getParticipants() {
        return  ImmutableList.of(target);
    }

}



