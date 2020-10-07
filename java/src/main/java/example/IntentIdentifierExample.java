package example;

import org.libra.AccountIdentifier;
import org.libra.AuthKey;
import org.libra.IntentIdentifier;
import org.libra.PrivateKey;
import org.libra.types.AccountAddress;
import org.libra.utils.CurrencyCode;
import org.libra.utils.Hex;

import static org.libra.AccountIdentifier.NetworkPrefix.TestnetPrefix;
import static org.libra.IntentIdentifier.decode;

/**
 * IntentIdentifierExample demonstrates the IntentIdentifier generation and deserialization
 */
public class IntentIdentifierExample {
    public static void main(String[] args) {
        //create IntentIdentifier
        PrivateKey privateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey authKey = GenerateKeysExample.generateAuthKey(privateKey);
        String intentIdentifierString = generateIntentIdentifier(authKey.accountAddress(), 130000000L, CurrencyCode.LBR);

        System.out.println(intentIdentifierString);

        //deserialize IntentIdentifier
        IntentIdentifier intentIdentifier = decodeIntentIdentifier(intentIdentifierString);
        System.out.println(Hex.encode(intentIdentifier.getAccountIdentifier().getAccountAddress().value));
        System.out.println(intentIdentifier.getAmount());
        System.out.println(intentIdentifier.getCurrency());
    }

    public static IntentIdentifier decodeIntentIdentifier(String intentIdentifierString) {
        return decode(TestnetPrefix, intentIdentifierString);
    }

    public static String generateIntentIdentifier(AccountAddress address, long amount, String currency) {
        AccountIdentifier accountIdentifier = new AccountIdentifier(TestnetPrefix, address);
        IntentIdentifier intentIdentifier = new IntentIdentifier(accountIdentifier, currency, amount);
        String encodedIntentIdentifier = intentIdentifier.encode();

        System.out.println("encoded IntentIdentifier: " + encodedIntentIdentifier);

        return encodedIntentIdentifier;
    }
}
