package example;

import org.libra.AuthKey;

import static org.libra.Testnet.mintCoinsAsync;

/**
 * MintExample demonstrates how to add currencies to account on the Libra blockchain
 * The mint also use to create new account by adding currencies base on new auth_key
 */
public class MintExample {
    public static void main(String[] args) {
        AuthKey authKey = GenerateKeysExample.generateAuthKey();

        //use mint to create new account
        mint(authKey, 192000000, "LBR");
    }

    public static void mint(AuthKey authKey, long amount, String currencyCode) {
        mintCoinsAsync(amount, authKey.hex(), currencyCode);
    }
}
