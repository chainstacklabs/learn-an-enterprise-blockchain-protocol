message: public(bytes[32])

@public
def __init__(): 
    self.message = "Hello, Block! In Vyper!"
    
@public
@constant
def printMessage() -> bytes[32]:
    return self.message

@public
def setMessage(_message: bytes[32]):
    self.message = _message
