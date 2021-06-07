pragma solidity ^0.6.1;

contract helloBlock {
    string message;
    constructor() public {
        message = "Hello, Block! In Solidity!";
    }

    function printMessage() public view returns (string memory) {
        return message;
    }

    function setMessage(string memory _message) public {
        message = _message;
    }
}
