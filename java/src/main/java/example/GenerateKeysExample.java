package example;

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import com.diem.AuthKey;
import com.diem.Ed25519PrivateKey;
import com.diem.utils.AccountAddressUtils;
import com.diem.utils.Hex;

import java.security.SecureRandom;

/**
 * GenerateKeysExample demonstrates the required steps to generate keys for an account on the Libra blockchain
 */
public class GenerateKeysExample {
    public static void main(String[] args) {
        //generate private key
        SecureRandom random = new SecureRandom();
        Ed25519PrivateKeyParameters privateKeyParams = new Ed25519PrivateKeyParameters(random);
        Ed25519PrivateKey privateKey = new Ed25519PrivateKey(privateKeyParams);

        //generate auth key
        AuthKey authKey = AuthKey.ed24419(privateKey.publicKey());
        String accountAddress = AccountAddressUtils.hex(authKey.accountAddress());

        System.out.println("Generated address: " + accountAddress);
        System.out.println("Auth Key (HEX): " + authKey.hex());
        System.out.println("Public key (HEX): " + Hex.encode(privateKey.publicKey()));
    }
}
