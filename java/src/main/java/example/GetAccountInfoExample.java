package example;

import org.libra.*;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.types.AccountAddress;

/**
 * GetAccountInfoExample demonstrates the required operation to retrieve account information from the Libra blockchain
 */
public class GetAccountInfoExample {
    public static final String CURRENCY_CODE = "Coin1";

    public static void main(String[] args) {
        //connect to testnet
        LibraClient client = Testnet.createClient();

        //create account
        PrivateKey privateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey authKey = GenerateKeysExample.generateAuthKey(privateKey);
        MintExample.mint(client, authKey, 1340000000, CURRENCY_CODE);

        //get account information
        Account account = getAccountInfo(client, authKey.accountAddress());

        System.out.println("~ Account info:");
        System.out.println(account);
    }

    public static Account getAccountInfo(LibraClient client, AccountAddress accountAddress) {
        try {
            return client.getAccount(accountAddress);
        } catch (LibraException e) {
            throw new RuntimeException(e);
        }
    }
}
