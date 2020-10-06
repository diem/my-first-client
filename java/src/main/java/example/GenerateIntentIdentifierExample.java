package example;

import org.libra.AccountIdentifier;
import org.libra.AuthKey;
import org.libra.IntentIdentifier;
import org.libra.PrivateKey;
import org.libra.types.AccountAddress;
import org.libra.utils.CurrencyCode;

import static org.libra.AccountIdentifier.NetworkPrefix.TestnetPrefix;

public class GenerateIntentIdentifierExample {
    public static void main(String[] args) {
        PrivateKey privateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey authKey = GenerateKeysExample.generateAuthKey(privateKey);
        String intentIdentifier = generateIntentIdentifier(authKey.accountAddress(), 130000000L, CurrencyCode.LBR);

        System.out.println(intentIdentifier);
    }

    public static String generateIntentIdentifier(AccountAddress address, long amount, String currency) {
        AccountIdentifier accountIdentifier = new AccountIdentifier(TestnetPrefix, address);
        IntentIdentifier intentIdentifier = new IntentIdentifier(accountIdentifier, currency, amount);
        String encodedIntentIdentifier = intentIdentifier.encode();

        System.out.println("encodedIntentIdentifier: " + encodedIntentIdentifier);

        return encodedIntentIdentifier;
    }
}
