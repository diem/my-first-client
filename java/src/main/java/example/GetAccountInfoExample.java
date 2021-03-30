package example;

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import com.diem.*;
import com.diem.jsonrpc.JsonRpc.Account;

import java.security.SecureRandom;

/**
 * GetAccountInfoExample demonstrates the required operation to retrieve account information from the Libra blockchain
 */
public class GetAccountInfoExample {
    public static final String CURRENCY_CODE = "XUS";

    public static void main(String[] args) throws DiemException {
        //connect to testnet
        DiemClient client = Testnet.createClient();

        //generate private key for new account
        PrivateKey privateKey = new Ed25519PrivateKey(new Ed25519PrivateKeyParameters(new SecureRandom()));
        //generate auth key for new account
        AuthKey authKey = AuthKey.ed25519(privateKey.publicKey());
        //create account
        Testnet.mintCoins(client, 1000000000, authKey.hex(), CURRENCY_CODE);

        //get account information
        Account account = client.getAccount(authKey.accountAddress());

        System.out.println("Account info:");
        System.out.println(account);
    }
}
