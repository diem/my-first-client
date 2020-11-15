package example;

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.libra.*;
import org.libra.utils.Hex;

import java.security.SecureRandom;

import static org.libra.AccountIdentifier.NetworkPrefix.TestnetPrefix;
import static org.libra.IntentIdentifier.decode;

/**
 * IntentIdentifierExample demonstrates the IntentIdentifier generation and deserialization
 */
public class IntentIdentifierExample {
    public static final String CURRENCY = "Coin1";

    public static void main(String[] args) {
        //generate private key for new account
        PrivateKey privateKey = new Ed25519PrivateKey(new Ed25519PrivateKeyParameters(new SecureRandom()));
        //generate auth key for new account
        AuthKey authKey = AuthKey.ed24419(privateKey.publicKey());
        //create IntentIdentifier
        AccountIdentifier accountIdentifier = new AccountIdentifier(TestnetPrefix, authKey.accountAddress());
        IntentIdentifier intentIdentifier = new IntentIdentifier(accountIdentifier, CURRENCY, 10000000L);
        String intentIdentifierString = intentIdentifier.encode();

        System.out.println("Encoded IntentIdentifier: " + intentIdentifierString);

        //deserialize IntentIdentifier
        IntentIdentifier decodedIntentIdentifier = decode(TestnetPrefix, intentIdentifierString);

        System.out.println("Account (HEX) from intent: " + Hex.encode(decodedIntentIdentifier.getAccountIdentifier().getAccountAddress().value));
        System.out.println("Amount from intent: " + decodedIntentIdentifier.getAmount());
        System.out.println("Currency from intent: " + decodedIntentIdentifier.getCurrency());
    }
}
