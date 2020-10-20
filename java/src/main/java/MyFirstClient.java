import example.*;
import org.libra.*;
import org.libra.jsonrpctypes.JsonRpc.Account;

public class MyFirstClient {
    //the default currency on testnet
    public static final String CURRENCY_CODE = "Coin1";

    /**
     * This code demonstrates basic flow for working with the LibraClient.
     * 1. Connect to testnet
     * 2. Generate keys
     * 3. Create account - by minting
     * 4. Get account information
     * 5. Start events listener
     * 6. Add money to existing account (mint)
     * 7. Generate keys for second account
     * 8. Create second account (for the benefit of the following transaction)
     * 9. Generate IntentIdentifier
     * 10. Deserialize IntentIdentifier
     * 11. Transfer money between accounts (peer to peer transaction)
     */
    public static void main(String[] args) {
        System.out.println("#1 Connect to testnet");
        LibraClient client = Testnet.createClient();

        System.out.println("#2 Generate Keys");
        PrivateKey senderPrivateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey senderAuthKey = GenerateKeysExample.generateAuthKey(senderPrivateKey);

        System.out.println("#3 Create account");
        MintExample.mint(client, senderAuthKey, 1340000000, CURRENCY_CODE);

        System.out.println("#4 Get account information");
        Account senderAccount = GetAccountInfoExample.getAccountInfo(client, senderAuthKey.accountAddress());

        String eventsKey = senderAccount.getReceivedEventsKey();
        System.out.println("#5 Subscribe to events stream");
        GetEventsExample.subscribe(client, eventsKey);

        System.out.println("#6 Add money to account");
        MintExample.mint(client, senderAuthKey, 270000000, CURRENCY_CODE);

        System.out.println("#7 Generate Keys");
        PrivateKey receiverPrivateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey receiverAuthKey = GenerateKeysExample.generateAuthKey(receiverPrivateKey);

        System.out.println("#8 Create second account");
        MintExample.mint(client, receiverAuthKey, 2560000000L, CURRENCY_CODE);

        System.out.println("#9 Generate IntentIdentifier");
        String intentIdentifierString = IntentIdentifierExample.generateIntentIdentifier(receiverAuthKey.accountAddress(), 130000000L, CURRENCY_CODE);

        System.out.println("#10 Deserialize IntentIdentifier");
        IntentIdentifier intentIdentifier = IntentIdentifierExample.decodeIntentIdentifier(intentIdentifierString);

        System.out.println("#11 Peer 2 peer transaction");
        SubmitPeerToPeerTransactionExample.submitPeerToPeerTransaction(client, senderPrivateKey,
                intentIdentifier.getAmount(),
                intentIdentifier.getAccountIdentifier().getAccountAddress(),
                senderAuthKey.accountAddress(),
                senderAccount.getSequenceNumber(),
                intentIdentifier.getCurrency());
    }
}
