package org.hyperledger;

import java.util.List;

import com.google.protobuf.ByteString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;

import static java.nio.charset.StandardCharsets.UTF_8;

// The start of the helloBlock chaincode implementation
public class helloBlock extends ChaincodeBase {

    private static Log _logger = LogFactory.getLog(helloBlock.class);

    // Initializing the chaincode so that it starts with a state
    @Override
    public Response init(ChaincodeStub stub) {
        try {
            _logger.info("helloBlock initialization");
            List<String> args = stub.getParameters();
            String state = args.get(0);

            _logger.info(String.format("state %s", state));

            // Write the initialized state to the ledger
            stub.putStringState(state, args.get(0));

            return newSuccessResponse();
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
    }

    // Invoking the chaincode to write a state to the ledger
    @Override
    public Response invoke(ChaincodeStub stub) {
        try {
            _logger.info("Invoking helloBlock");
            String func = stub.getFunction();
            List<String> params = stub.getParameters();
            if (func.equals("invoke")) {
                return invoke(stub, params);
            }
            if (func.equals("query")) {
                return query(stub, params);
            }
            return newErrorResponse("Invalid command. Use [\"invoke\" or \"query\"]");
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
    }

    private Response invoke(ChaincodeStub stub, List<String> args) {

        String stateFromKey = args.get(0);

        // Get the state from the ledger
        String stateFromValueStr = stub.getStringState(stateFromKey);
        if (stateFromValueStr == null) {
            return newErrorResponse(String.format("State %s not found", stateFromKey));
        }

        // Write the state back to the ledger
        stub.putStringState(stateFromKey,stateFromKey);

        _logger.info("State put on the ledger");

        return newSuccessResponse("Invoke successful", ByteString.copyFrom(stateFromKey + ": ", UTF_8).toByteArray());
    }

    // Query the state
    private Response query(ChaincodeStub stub, List<String> args) {

        String key = args.get(0);
        // Get the state from the ledger
        String val	= stub.getStringState(key);
        if (val == null) {
            return newErrorResponse(String.format("No state for %s", key));
        }
        _logger.info(String.format("Query response: %s", key, val));
        return newSuccessResponse(val, ByteString.copyFrom(val, UTF_8).toByteArray());
    }

    public static void main(String[] args) {
        new helloBlock().start(args);
    }

}
