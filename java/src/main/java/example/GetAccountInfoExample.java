package example;

import org.libra.AuthKey;
import org.libra.LibraClient;
import org.libra.LibraException;
import org.libra.PrivateKey;
import org.libra.jsonrpc.LibraJsonRpcClient;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.utils.CurrencyCode;

public class GetAccountInfoExample {
    public static void main(String[] args) {
        PrivateKey privateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey authKey = GenerateKeysExample.generateAuthKey(privateKey);
        MintExample.mint(authKey, "1340000000", CurrencyCode.LBR);
        String accountAddress = GenerateKeysExample.extractAccountAddress(authKey);

        Account account = getAccountInfo(accountAddress);

        System.out.println(account);
    }

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