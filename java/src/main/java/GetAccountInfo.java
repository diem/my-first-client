import org.libra.LibraClient;
import org.libra.LibraException;
import org.libra.jsonrpc.LibraJsonRpcClient;
import org.libra.jsonrpctypes.JsonRpc.Account;

public class GetAccountInfo {
    public static Account getAccountInfo(String accountAddress) {
        //Connect to testnet
        LibraClient client = new LibraJsonRpcClient(Main.NET_URL, Main.CHAIN_ID);

        Account account;

        try {
            account = client.getAccount(accountAddress);

            System.out.println(account);
        } catch (LibraException e) {
            throw new RuntimeException(e);
        }

        return account;
    }
}
