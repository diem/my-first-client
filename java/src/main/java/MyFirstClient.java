import example.*;
import org.libra.AuthKey;
import org.libra.IntentIdentifier;
import org.libra.PrivateKey;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.utils.CurrencyCode;

public class MyFirstClient {
    /**
     * This code demonstrates basic flow for working with the LibraClient.
     * 1. Generate keys
     * 2. Create account - by minting
     * 3. Get account information
     * 4. Start events listener
     * 5. Add money to existing account (mint)
     * 6. Generate keys for second account
     * 7. Create second account (for the benefit of the following transaction)
     * 8. Generate IntentIdentifier
     * 9. Deserialize IntentIdentifier
     * 10. Transfer money between accounts (peer to peer transaction)
     */
    public static void main(String[] args) {
        System.out.println("#1 Generate Keys");
        PrivateKey senderPrivateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey senderAuthKey = GenerateKeysExample.generateAuthKey(senderPrivateKey);

        System.out.println("#2 Create account");
        MintExample.mint(senderAuthKey, "1340000000", CurrencyCode.LBR);

        String senderAccountAddress = GenerateKeysExample.extractAccountAddress(senderAuthKey);

        System.out.println("#3 Get account information");
        Account senderAccount = GetAccountInfoExample.getAccountInfo(senderAccountAddress);

        String eventsKey = senderAccount.getReceivedEventsKey();
        System.out.println("#4 Subscribe to events stream");
        GetEventsExample.subscribe(eventsKey);

        System.out.println("#5 Add money to account");
        MintExample.mint(senderAuthKey, "270000000", CurrencyCode.LBR);

        System.out.println("#6 Generate Keys");
        PrivateKey receiverPrivateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey receiverAuthKey = GenerateKeysExample.generateAuthKey(receiverPrivateKey);

        System.out.println("#7 Create second account");
        MintExample.mint(receiverAuthKey, "2560000000", CurrencyCode.LBR);

        System.out.println("#8 Generate IntentIdentifier");
        String intentIdentifierString = IntentIdentifierExample.generateIntentIdentifier(receiverAuthKey.accountAddress(), 130000000L, CurrencyCode.LBR);

        System.out.println("#9 Deserialize IntentIdentifier");
        IntentIdentifier intentIdentifier = IntentIdentifierExample.decodeIntentIdentifier(intentIdentifierString);

        System.out.println("#10 Peer 2 peer transaction");
        SubmitPeerToPeerTransactionExample.submitPeerToPeerTransaction(senderPrivateKey,
                intentIdentifier.getAmount(),
                intentIdentifier.getAccountIdentifier().getAccountAddress(),
                senderAuthKey.accountAddress(),
                senderAccount.getSequenceNumber(),
                intentIdentifier.getCurrency());
    }
}
