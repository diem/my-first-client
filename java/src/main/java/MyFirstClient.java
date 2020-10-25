import com.novi.serde.Bytes;
import example.GetEventsExample;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.libra.*;
import org.libra.jsonrpc.StaleResponseException;
import org.libra.jsonrpctypes.JsonRpc;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.stdlib.Helpers;
import org.libra.types.RawTransaction;
import org.libra.types.SignedTransaction;
import org.libra.types.TransactionPayload;
import org.libra.utils.CurrencyCode;

import java.security.SecureRandom;

import static org.libra.AccountIdentifier.NetworkPrefix.TestnetPrefix;
import static org.libra.IntentIdentifier.decode;
import static org.libra.Testnet.CHAIN_ID;

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
    public static void main(String[] args) throws LibraException {
        System.out.println("#1 Connect to testnet");
        LibraClient client = Testnet.createClient();

        System.out.println("#2 Generate Keys");
        PrivateKey senderPrivateKey = new Ed25519PrivateKey(new Ed25519PrivateKeyParameters(new SecureRandom()));
        AuthKey senderAuthKey = AuthKey.ed24419(senderPrivateKey.publicKey());

        System.out.println("#3 Create account");
        Testnet.mintCoins(client, 1340000000, senderAuthKey.hex(), CURRENCY_CODE);

        System.out.println("#4 Get account information");
        Account senderAccount = client.getAccount(senderAuthKey.accountAddress());

        String eventsKey = senderAccount.getReceivedEventsKey();
        System.out.println("#5 Subscribe to events stream");
        GetEventsExample.subscribe(client, eventsKey);

        System.out.println("#6 Add money to account");
        Testnet.mintCoins(client, 270000000, senderAuthKey.hex(), CURRENCY_CODE);

        System.out.println("#7 Generate Keys");
        //generate private key for new account
        PrivateKey receiverPrivateKey = new Ed25519PrivateKey(new Ed25519PrivateKeyParameters(new SecureRandom()));
        //generate auth key for new account
        AuthKey receiverAuthKey = AuthKey.ed24419(receiverPrivateKey.publicKey());

        System.out.println("#8 Create second account");
        Testnet.mintCoins(client, 2560000000L, receiverAuthKey.hex(), CURRENCY_CODE);

        System.out.println("#9 Generate IntentIdentifier");
        AccountIdentifier accountIdentifier = new AccountIdentifier(TestnetPrefix, receiverAuthKey.accountAddress());
        IntentIdentifier intentIdentifier = new IntentIdentifier(accountIdentifier, CURRENCY_CODE, 130000000L);
        String intentIdentifierString = intentIdentifier.encode();

        System.out.println("~ Encoded IntentIdentifier: " + intentIdentifierString);

        System.out.println("#10 Deserialize IntentIdentifier");
        IntentIdentifier decodedIntentIdentifier = decode(TestnetPrefix, intentIdentifierString);

        System.out.println("#11 Peer 2 peer transaction");
        //Create script
        TransactionPayload script = new TransactionPayload.Script(
                Helpers.encode_peer_to_peer_with_metadata_script(
                        CurrencyCode.typeTag(decodedIntentIdentifier.getCurrency()),
                        decodedIntentIdentifier.getAccountIdentifier().getAccountAddress(),
                        decodedIntentIdentifier.getAmount(),
                        new Bytes(new byte[0]),
                        new Bytes(new byte[0])));
        //Create transaction
        RawTransaction rawTransaction = new RawTransaction(
                senderAuthKey.accountAddress(),
                senderAccount.getSequenceNumber(),
                script,
                1000000L,
                0L,
                CURRENCY_CODE,
                (System.currentTimeMillis() / 1000) + 300,
                CHAIN_ID);
        //Sign transaction
        SignedTransaction st = Signer.sign(senderPrivateKey, rawTransaction);
        //Submit transaction
        try {
            client.submit(st);
        } catch (StaleResponseException e) {
            //ignore
        }
        //Wait for the transaction to completenew SecureRandom()
        try {
            JsonRpc.Transaction transaction = client.waitForTransaction(st, 100000);

            System.out.println(transaction);
        } catch (LibraException e) {
            throw new RuntimeException("Failed while waiting to transaction ", e);
        }
    }
}
