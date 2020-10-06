import example.*;
import org.libra.AuthKey;
import org.libra.IntentIdentifier;
import org.libra.PrivateKey;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.jsonrpctypes.JsonRpc.Event;
import org.libra.utils.CurrencyCode;

import java.util.Queue;

import static org.libra.AccountIdentifier.NetworkPrefix.TestnetPrefix;
import static org.libra.IntentIdentifier.decode;

public class MyFirstClient {
    /**
     * This code demonstrates basic flow for working with the LibraClient.
     * 1. Generate keys
     * 2. Create account - by minting
     * 3. Get account information
     * 4. Start events listener
     * 5. Get new events (1)
     * 6. Add money to existing account (mint)
     * 7. Get new events (2)
     * 8. Generate keys for second account
     * 9. Create second account (for the benefit of the following transaction)
     * 10. Generate IntentIdentifier
     * 11. Deserialize IntentIdentifier
     * 12. Transfer money between accounts (peer to peer transaction)
     * 13. Get new events (3)
     * 14. Close listener
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
        System.out.println("#4 Start event listener");
        GetEventsExample getEventsExample = new GetEventsExample();
        getEventsExample.start(eventsKey);

        System.out.println("#5 Get new events (1)");
        Queue<Event> newEvents = getEventsExample.get(eventsKey);
        System.out.println(newEvents.size() + " new events was found");

        System.out.println("#6 Add money to account");
        MintExample.mint(senderAuthKey, "270000000", CurrencyCode.LBR);

        System.out.println("#7 Get new events (2)");
        newEvents = getEventsExample.get(eventsKey);
        System.out.println(newEvents.size() + " new events was found");

        System.out.println("#8 Generate Keys");
        PrivateKey receiverPrivateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey receiverAuthKey = GenerateKeysExample.generateAuthKey(receiverPrivateKey);

        System.out.println("#9 Create second account");
        MintExample.mint(receiverAuthKey, "2560000000", CurrencyCode.LBR);

        System.out.println("#10 Generate IntentIdentifier");
        String intentIdentifierString = GenerateIntentIdentifierExample.generateIntentIdentifier(receiverAuthKey.accountAddress(), 130000000L, CurrencyCode.LBR);

        System.out.println("#11 Deserialize IntentIdentifier");
        IntentIdentifier intentIdentifier = decode(TestnetPrefix, intentIdentifierString);

        System.out.println("#12 Peer 2 peer transaction");
        SubmitPeerToPeerTransactionExample.submitPeerToPeerTransaction(senderPrivateKey,
                intentIdentifier.getAmount(),
                intentIdentifier.getAccountIdentifier().getAccountAddress(),
                senderAuthKey.accountAddress(),
                senderAccount.getSequenceNumber(),
                intentIdentifier.getCurrency());

        System.out.println("#13 Get new events (3)");
        newEvents = getEventsExample.get(eventsKey);
        System.out.println(newEvents.size() + " new events was found");

        System.out.println("#14 Stop event listener");
        getEventsExample.stop();
    }
}
