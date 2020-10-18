package example;

import com.novi.serde.Bytes;
import org.libra.*;
import org.libra.jsonrpc.LibraJsonRpcClient;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.jsonrpctypes.JsonRpc.Transaction;
import org.libra.stdlib.Helpers;
import org.libra.types.AccountAddress;
import org.libra.types.RawTransaction;
import org.libra.types.SignedTransaction;
import org.libra.types.TransactionPayload;
import org.libra.utils.CurrencyCode;

import static org.libra.Testnet.CHAIN_ID;
import static org.libra.Testnet.JSON_RPC_URL;

/**
 * SubmitPeerToPeerTransactionExample demonstrates currencies transfer between 2 accounts on the Libra blockchain
 */
public class SubmitPeerToPeerTransactionExample {
    public static void main(String[] args) {
        //create sender account
        PrivateKey senderPrivateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey senderAuthKey = GenerateKeysExample.generateAuthKey(senderPrivateKey);
        MintExample.mint(senderAuthKey, 1340000000, "Coin1");

        //get sender account sequence number
        Account account = GetAccountInfoExample.getAccountInfo(senderAuthKey.accountAddress());

        //create receiver account
        AuthKey receiverAuthKey = GenerateKeysExample.generateAuthKey();
        MintExample.mint(senderAuthKey, 100000000, "Coin1");

        submitPeerToPeerTransaction(
                senderPrivateKey,
                130000000L,
                receiverAuthKey.accountAddress(),
                senderAuthKey.accountAddress(),
                account.getSequenceNumber(),
                "Coin1");
    }

    public static void submitPeerToPeerTransaction(PrivateKey privateKey,
                                                   long amount,
                                                   AccountAddress receiverAccountAddress,
                                                   AccountAddress senderAccountAddress,
                                                   long sequenceNumber,
                                                   String currencyCode) {
        //Connect to testnet
        LibraClient client = new LibraJsonRpcClient(JSON_RPC_URL, CHAIN_ID);
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
                "Coin1",
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
