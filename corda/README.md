# Corda

You will create, deploy, and run a “Hello, Block!” CorDapp on Corda written Kotlin and Java.

## Architecture

On Corda, a smart contract is called a CorDapp. Each CorDapp consists of a contract and a flow. The contract establishes the transaction validity, and the flow automates the logic of the exchange between the involved nodes.

A CorDapp must be installed on each of the nodes involved in an exchange. Unlike in public blockchain protocols, the smart contracts are not propagated to all nodes in the network.

Each exchange is notarized by a separate node called the notary.

![corda_architecture](https://user-images.githubusercontent.com/10195782/120948851-b19eaf80-c775-11eb-9a63-50305639d20c.jpg)

## Ecosystem

Corda is mainly driven by R3 developers—R3 is the company behind Corda.

## Prerequisites

1. A [Chainstack account](https://console.chainstack.com/).
1. Two Corda nodes. See [Deploy a consortium network](https://docs.chainstack.com/platform/deploy-a-consortium-network) and [Add a node to a network](https://docs.chainstack.com/platform/add-a-node-to-a-network).

## CorDapp code

See the respective CorDapp source code directory.

## Build the contract and the flow

To build the contract and the flow, run `./gradlew build` in the root of the respective directory.

This will build the contract and the flow JAR files:

* `/contract/build/libs/contracts-0.1.jar`
* `/workflows/build/libs/workflows-0.1.jar`

## Install the contract and the flow

Install both the contract and the flow on each of the two nodes you deployed. See [Installing a CorDapp](https://docs.chainstack.com/operations/corda/installing-a-cordapp).

## Connect to one of the nodes

To be able to interact with the CorDapps, you must have the contract and the flow JAR files both locally on your machine and on the node you are connecting to.

Copy the previously built contract and flow JAR files to a directory on your machine.

Connect to one of your nodes while providing the directory with the JAR files on your local machine. For detailed instructions, see [Corda interaction tools](https://docs.chainstack.com/operations/corda/tools#interaction-tools).

## Interact with the CorDapp

In the shell, run:

``` bash
start helloBlockFlow target: "LEGAL_NAME"
```

where

* LEGAL_NAME — the legal name of your second Corda to which you are sending a “Hello, Block!” transaction. To get the node’s legal name, see [View node access and credentials](https://docs.chainstack.com/platform/view-node-access-and-credentials).

Example:

``` bash
start helloBlockFlow target: "OU=RG-254-409-ND-141-587-071, O=RG-254-409, L=Portland, C=US, ST=Oregon"
```

Output:

``` bash
 ✓ Starting
          Requesting signature by notary service
              Requesting signature by Notary service
              Validating response from Notary service
     ✓ Broadcasting transaction to participants
▶︎ Done
Flow completed with result: SignedTransaction(id=F2B267806C09BDEEB0E1E815AC1502EFFCD8C4F19F5701CFE9BECC46871AE47C)
```

Congratulations! What you have just done is: a) deployed a two-node private Corda network; b) built a smart contract in the programming language you are comfortable with; c) interacted with it on your Corda network.

And it wasn’t even complicated.
