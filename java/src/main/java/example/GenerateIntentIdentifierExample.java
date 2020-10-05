package example;

import org.libra.AccountIdentifier;
import org.libra.IntentIdentifier;
import org.libra.types.AccountAddress;

import static org.libra.AccountIdentifier.NetworkPrefix.TestnetPrefix;

public class GenerateIntentIdentifierExample {
    public static String generateIntentIdentifier(AccountAddress address, long amount, String currency) {
        AccountIdentifier accountIdentifier = new AccountIdentifier(TestnetPrefix, address);
        IntentIdentifier intentIdentifier = new IntentIdentifier(accountIdentifier, currency, amount);
        String encodedIntentIdentifier = intentIdentifier.encode();

        System.out.println("encodedIntentIdentifier: " + encodedIntentIdentifier);

        return encodedIntentIdentifier;
    }
}
