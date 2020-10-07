package example;

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.libra.AuthKey;
import org.libra.Ed25519PrivateKey;
import org.libra.PrivateKey;
import org.libra.types.AccountAddress;
import org.libra.utils.Hex;

import java.security.SecureRandom;

public class GenerateKeysExample {
    public static PrivateKey generatePrivateKey() {
        SecureRandom random = new SecureRandom();
        Ed25519PrivateKeyParameters privateKeyParams = new Ed25519PrivateKeyParameters(random);

        return new Ed25519PrivateKey(privateKeyParams);
    }

    public static AuthKey generateAuthKey(PrivateKey privateKey) {
        AuthKey authKey = AuthKey.ed24419(privateKey.publicKey());
        String accountAddress = extractAccountAddress(authKey);

        System.out.println("~ Generated address: " + accountAddress);

        return authKey;
    }

    public static AuthKey generateAuthKey() {
        PrivateKey privateKey = generatePrivateKey();

        return generateAuthKey(privateKey);
    }

    public static String extractAccountAddress(AuthKey authKey) {
        AccountAddress accountAddress = authKey.accountAddress();

        return Hex.encode(accountAddress.value);
    }
}
