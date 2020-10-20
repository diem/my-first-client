package example;

import com.novi.serde.Bytes;
import org.libra.*;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.jsonrpctypes.JsonRpc.Transaction;
import org.libra.stdlib.Helpers;
import org.libra.types.AccountAddress;
import org.libra.types.RawTransaction;
import org.libra.types.SignedTransaction;
import org.libra.types.TransactionPayload;
import org.libra.utils.CurrencyCode;

import static org.libra.Testnet.CHAIN_ID;

/**
 * SubmitPeerToPeerTransactionExample demonstrates currencies transfer between 2 accounts on the Libra blockchain
 */
public class SubmitPeerToPeerTransactionExample {
    public static final String CURRENCY_CODE = "Coin1";

    public static void main(String[] args) {
        //connect to testnet
        LibraClient client = Testnet.createClient();

        //create sender account
        PrivateKey senderPrivateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey senderAuthKey = GenerateKeysExample.generateAuthKey(senderPrivateKey);
        MintExample.mint(client, senderAuthKey, 1340000000, CURRENCY_CODE);

        //get sender account for sequence number
        Account account = GetAccountInfoExample.getAccountInfo(client, senderAuthKey.accountAddress());

        //create receiver account
        AuthKey receiverAuthKey = GenerateKeysExample.generateAuthKey();
        MintExample.mint(client, receiverAuthKey, 120000000, CURRENCY_CODE);

        submitPeerToPeerTransaction(client,
                senderPrivateKey,
                130000000L,
                receiverAuthKey.accountAddress(),
                senderAuthKey.accountAddress(),
                account.getSequenceNumber(),
                CURRENCY_CODE);
    }

    public static void submitPeerToPeerTransaction(LibraClient client,
                                                   PrivateKey privateKey,
                                                   long amount,
                                                   AccountAddress receiverAccountAddress,
                                                   AccountAddress senderAccountAddress,
                                                   long sequenceNumber,
                                                   String currencyCode) {
        //Create script
        TransactionPayload script = new TransactionPayload.Script(
                Helpers.encode_peer_to_peer_with_metadata_script(
                        CurrencyCode.typeTag(currencyCode),
                        receiverAccountAddress,
                        amount,
                        new Bytes(new byte[0]),
                        new Bytes(new byte[0])));
        //Create transaction
        RawTransaction rawTransaction = new RawTransaction(
                senderAccountAddress,
                sequenceNumber,
                script,
                1000000L,
                0L,
                CURRENCY_CODE,
                (System.currentTimeMillis() / 1000) + 300,
                CHAIN_ID);
        //Sign transaction
        SignedTransaction st = Signer.sign(privateKey, rawTransaction);
        //Submit transaction
        try {
            client.submit(st);
        } catch (LibraException e) {
            throw new RuntimeException("Failed to submit transaction", e);
        }
        //Wait for the transaction to complete
        try {
            Transaction transaction = client.waitForTransaction(st, 100000);

            System.out.println(transaction);
        } catch (LibraException e) {
            throw new RuntimeException("Failed while waiting to transaction ", e);
        }
    }
}
