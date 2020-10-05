import example.*;
import org.libra.AuthKey;
import org.libra.PrivateKey;
import org.libra.jsonrpctypes.JsonRpc;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.utils.CurrencyCode;

import java.util.Queue;

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
     * 10. Transfer money between accounts (peer to peer transaction)
     * 11. Get new events (3)
     * 12. Close listener
     */
    public static void main(String[] args) {
        System.out.println("#1 Generate Keys");
        PrivateKey privateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey authKey = GenerateKeysExample.generateAuthKey(privateKey);

        System.out.println("#2 Create account");
        MintExample.mint(authKey, "1340000000", CurrencyCode.LBR);

        String accountAddress = GenerateKeysExample.extractAccountAddress(authKey);

        System.out.println("#3 Get account information");
        Account account = GetAccountInfoExample.getAccountInfo(accountAddress);

        System.out.println("#4 Start event listener");
        GetEventsExample getEventsExample = new GetEventsExample();
        getEventsExample.start(account.getReceivedEventsKey());

        System.out.println("#5 Get new events (1)");
        Queue<GetEventsExample.EventExample> newEvents = getEventsExample.get(account.getReceivedEventsKey());
        System.out.println(newEvents.size() + " new events was found");

        System.out.println("#6 Add money to account");
        MintExample.mint(authKey, "270000000", CurrencyCode.LBR);

        System.out.println("#7 Get new events (2)");
        newEvents = getEventsExample.get(account.getReceivedEventsKey());
        System.out.println(newEvents.size() + " new events was found");

        System.out.println("#8 Generate Keys");
        PrivateKey receiverPrivateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey receiverAuthKey = GenerateKeysExample.generateAuthKey(receiverPrivateKey);

        System.out.println("#9 Create second account");
        MintExample.mint(receiverAuthKey, "2560000000", CurrencyCode.LBR);

        System.out.println("#10 Peer 2 peer transaction");
        SubmitPeerToPeerTransactionExample.submitPeerToPeerTransaction(privateKey, authKey, account, receiverAuthKey);

        System.out.println("#11 Get new events (3)");
        newEvents = getEventsExample.get(account.getReceivedEventsKey());
        System.out.println(newEvents.size() + " new events was found");

        System.out.println("#12 Stop event listener");
        getEventsExample.stop();
    }
}
