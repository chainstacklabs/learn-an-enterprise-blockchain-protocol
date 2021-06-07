
const shim = require('fabric-shim');

// The start of the helloBlock chaincode implementation
var helloBlock = class {

  // Initializing the chaincode so that it starts with a state
  async Init(stub) {
    console.info('helloBlock initialization');
    let ret = stub.getFunctionAndParameters();
    console.info(ret);
    let args = ret.params;

    let state = args[0];
    
    // Write the initialized state to the ledger
    try {
      await stub.putState(state, args[0]);
      return shim.success();
    } catch (err) {
      return shim.error(err);
    }
  }

  // Invoking the chaincode to write a state to the ledger
  async Invoke(stub) {
    let ret = stub.getFunctionAndParameters();
    console.info(ret);
    let method = this[ret.fcn];
    if (!method) {
      console.log('No' + ret.fcn + ' command found');
      return shim.success();
    }
    try {
      let payload = await method(stub, ret.params);
      return shim.success(payload);
    } catch (err) {
      console.log(err);
      return shim.error(err);
    }
  }

  async invoke(stub, args) {

    let state = args[0];

    if (!state) {
      throw new Error('State cannot be empty');
    }

    // Get the state from the ledger
    let statebytes = await stub.getState(state);
    if (!statebytes) {
      throw new Error('Cannot get state');
    }

    // Write the state back to the ledger
    await stub.putState(state, args[0]);

  }

  // Query the state
  async query(stub, args) {
    let jsonResp = {};
    let state = args[0];

    // Get the state from the ledger
    let statebytes = await stub.getState(state);
    if (!statebytes) {
      jsonResp.error = 'Cannot get state for ' + state;
      throw new Error(JSON.stringify(jsonResp));
    }

    jsonResp.name = state;
    jsonResp.amount = statebytes.toString();
    console.info('Query response:');
    console.info(jsonResp);
    return statebytes;
  }
};

shim.start(new helloBlock());
