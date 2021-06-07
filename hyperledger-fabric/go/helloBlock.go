package main

import (
	"errors"
	"fmt"

	"github.com/hyperledger/fabric-contract-api-go/contractapi"
)

// The start of the helloBlock chaincode implementation
type helloBlock struct {
	contractapi.Contract
}

// Initializing the chaincode so that it starts with a state
func (t *helloBlock) Init(ctx contractapi.TransactionContextInterface, state string) error {
	fmt.Println("helloBlock initialization")
	var err error
	fmt.Printf("State %s", state)
	// Write the initialized state to the ledger
	err = ctx.GetStub().PutState(state, []byte(state))
	if err != nil {
		return err
	}

	return nil
}

// Invoking the chaincode to write a state to the ledger
func (t *helloBlock) Invoke(ctx contractapi.TransactionContextInterface, state string) error {
	var err error
	// Get the state from the ledger
	statebytes, err := ctx.GetStub().GetState(state)
	if err != nil {
		return fmt.Errorf("Cannot get state")
	}
	if statebytes == nil {
		return fmt.Errorf("No state found")
	}

	fmt.Printf("State %s", state)

	// Write the state back to the ledger
	err = ctx.GetStub().PutState(state, []byte(state))
	if err != nil {
		return err
	}

	return nil
}

// Query the state
func (t *helloBlock) Query(ctx contractapi.TransactionContextInterface, state string) (string, error) {
	var err error
	// Get the state from the ledger
	statebytes, err := ctx.GetStub().GetState(state)
	if err != nil {
		jsonResp := "{\"Cannot get state for " + state + "\"}"
		return "", errors.New(jsonResp)
	}

	if statebytes == nil {
		jsonResp := "{\"No state for " + state + "\"}"
		return "", errors.New(jsonResp)
	}

	jsonResp := "{\"" + state + "\",\"" + string(statebytes) + "\"}"
	fmt.Printf("Query response:%s\n", jsonResp)
	return string(statebytes), nil
}

func main() {
	cc, err := contractapi.NewChaincode(new(helloBlock))
	if err != nil {
		panic(err.Error())
	}
	if err := cc.Start(); err != nil {
		fmt.Printf("Error starting helloBlock chaincode: %s", err)
	}
}
