import example.*;
import org.libra.AuthKey;
import org.libra.PrivateKey;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.utils.CurrencyCode;

public class Main {
    /**
     * This code demonstrates basic flow for working with the LibraClient.
     * 1. Generate keys
     * 2. Create account (by mint for new account base on the keys we generated)
     * 3. Get account information
     * 4. Add money to existing account (mint)
     * 5. Generate second account for the benefit of the following transaction
     * 6. Transfer money between accounts (peer to peer transaction)
     */
    public static void main(String args[]) {
        System.out.println("~1 Generate Keys");
        PrivateKey privateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey authKey = GenerateKeysExample.generateAuthKey(privateKey);

        System.out.println("~2 Create account");
        MintExample.mint(authKey, "1340000000", CurrencyCode.LBR);

        String accountAddress = GenerateKeysExample.extractAccountAddress(authKey);

        System.out.println("~3 Get account information");
        Account account = GetAccountInfoExample.getAccountInfo(accountAddress);

        System.out.println("~ Get new events 1");
        GetEventsExample.start(account);
        GetEventsExample.get(account);

        System.out.println("~4 Add money to account");
        MintExample.mint(authKey, "270000000", CurrencyCode.LBR);

        System.out.println("~ Get new events 2");
        GetEventsExample.get(account);

        System.out.println("~5 Create receiver account");
        PrivateKey receiverPrivateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey receiverAuthKey = GenerateKeysExample.generateAuthKey(receiverPrivateKey);

        MintExample.mint(receiverAuthKey, "2560000000", CurrencyCode.LBR);

        System.out.println("~ Get new events 3");
        GetEventsExample.get(account);

        System.out.println("~6 Peer 2 peer transaction");
        SubmitPeerToPeerTransactionExample.submitPeerToPeerTransaction(privateKey, authKey, account, receiverAuthKey);

        System.out.println("~ Get new events 4");
        GetEventsExample.get(account);

        GetEventsExample.stop();
    }
}
