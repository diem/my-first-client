package example;

import org.libra.AuthKey;
import org.libra.LibraClient;
import org.libra.Testnet;

/**
 * MintExample demonstrates how to add currencies to account on the Libra blockchain
 * The mint also use to create new account by adding currencies base on new auth_key
 */
public class MintExample {
    public static final String CURRENCY_CODE = "Coin1";

    public static void main(String[] args) {
        //connect to testnet
        LibraClient client = Testnet.createClient();

        AuthKey authKey = GenerateKeysExample.generateAuthKey();

        //use mint to create new account
        mint(client, authKey, 192000000, CURRENCY_CODE);
    }

    public static void mint(LibraClient client, AuthKey authKey, long amount, String currencyCode) {
        Testnet.mintCoins(client, amount, authKey.hex(), currencyCode);
    }
}
