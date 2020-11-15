package example;

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.libra.*;
import org.libra.utils.AccountAddressUtils;

import java.security.SecureRandom;

/**
 * MintExample demonstrates how to add currencies to account on the Libra blockchain
 * The mint also use to create new account by adding currencies base on new auth_key
 */
public class MintExample {
    public static final String CURRENCY_CODE = "Coin1";

    public static void main(String[] args) {
        //connect to testnet
        LibraClient client = Testnet.createClient();

        //generate private key for new account
        PrivateKey privateKey = new Ed25519PrivateKey(new Ed25519PrivateKeyParameters(new SecureRandom()));
        //generate auth key for new account
        AuthKey authKey = AuthKey.ed24419(privateKey.publicKey());
        String accountAddress = AccountAddressUtils.hex(authKey.accountAddress());

        System.out.println("~ Generated address: " + accountAddress);

        //use mint to create new account
        Testnet.mintCoins(client, 100000000, authKey.hex(), CURRENCY_CODE);
    }
}
