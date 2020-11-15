package example;

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.libra.*;
import org.libra.jsonrpctypes.JsonRpc.Account;

import java.security.SecureRandom;

/**
 * GetAccountInfoExample demonstrates the required operation to retrieve account information from the Libra blockchain
 */
public class GetAccountInfoExample {
    public static final String CURRENCY_CODE = "Coin1";

    public static void main(String[] args) throws LibraException {
        //connect to testnet
        LibraClient client = Testnet.createClient();

        //generate private key for new account
        PrivateKey privateKey = new Ed25519PrivateKey(new Ed25519PrivateKeyParameters(new SecureRandom()));
        //generate auth key for new account
        AuthKey authKey = AuthKey.ed24419(privateKey.publicKey());
        //create account
        Testnet.mintCoins(client, 1000000000, authKey.hex(), CURRENCY_CODE);

        //get account information
        Account account = client.getAccount(authKey.accountAddress());

        System.out.println("~ Account info:");
        System.out.println(account);
    }
}
