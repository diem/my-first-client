package example;

import org.libra.AuthKey;
import org.libra.LibraClient;
import org.libra.jsonrpc.LibraJsonRpcClient;

import static org.libra.Testnet.*;
import static org.libra.Testnet.CHAIN_ID;

/**
 * MintExample demonstrates how to add currencies to account on the Libra blockchain
 * The mint also use to create new account by adding currencies base on new auth_key
 */
public class MintExample {
    public static void main(String[] args) {
        AuthKey authKey = GenerateKeysExample.generateAuthKey();

        //use mint to create new account
        mint(authKey, 192000000, "Coin1");
    }

    public static void mint(AuthKey authKey, long amount, String currencyCode) {
        LibraClient client = new LibraJsonRpcClient(JSON_RPC_URL, CHAIN_ID);

        mintCoins(client, amount, authKey.hex(), currencyCode);
    }
}
