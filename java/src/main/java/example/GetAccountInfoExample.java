package example;

import org.libra.LibraClient;
import org.libra.LibraException;
import org.libra.jsonrpc.LibraJsonRpcClient;
import org.libra.jsonrpctypes.JsonRpc.Account;

public class GetAccountInfoExample {
    public static Account getAccountInfo(String accountAddress) {
        //Connect to testnet
        LibraClient client = new LibraJsonRpcClient(Testnet.NET_URL, Testnet.CHAIN_ID);

        try {
            return client.getAccount(accountAddress);
        } catch (LibraException e) {
            throw new RuntimeException(e);
        }
    }
}
