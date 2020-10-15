package example;

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.libra.AuthKey;
import org.libra.Ed25519PrivateKey;
import org.libra.PrivateKey;
import org.libra.types.AccountAddress;
import org.libra.utils.AccountAddressUtils;
import org.libra.utils.Hex;

import java.security.SecureRandom;

/**
 * GenerateKeysExample demonstrates the required steps to generate keys for an account on the Libra blockchain
 */
public class GenerateKeysExample {
    public static void main(String[] args) {
        PrivateKey privateKey = generatePrivateKey();

        AuthKey authKey = generateAuthKey(privateKey);

        System.out.println("~ Auth Key (HEX): " + authKey.hex());
        System.out.println("~ Public key (HEX): " + Hex.encode(privateKey.publicKey()));
    }

    public static PrivateKey generatePrivateKey() {
        SecureRandom random = new SecureRandom();
        Ed25519PrivateKeyParameters privateKeyParams = new Ed25519PrivateKeyParameters(random);

        return new Ed25519PrivateKey(privateKeyParams);
    }

    public static AuthKey generateAuthKey(PrivateKey privateKey) {
        AuthKey authKey = AuthKey.ed24419(privateKey.publicKey());
        String accountAddress = AccountAddressUtils.hex(authKey.accountAddress());

        System.out.println("~ Generated address: " + accountAddress);

        return authKey;
    }

    public static AuthKey generateAuthKey() {
        PrivateKey privateKey = generatePrivateKey();

        return generateAuthKey(privateKey);
    }
}
