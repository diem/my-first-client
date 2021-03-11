package example;

import com.novi.serde.Bytes;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import com.diem.*;
import com.diem.jsonrpc.StaleResponseException;
import com.diem.jsonrpc.JsonRpc.Account;
import com.diem.jsonrpc.JsonRpc.Transaction;
import com.diem.stdlib.Helpers;
import com.diem.types.RawTransaction;
import com.diem.types.SignedTransaction;
import com.diem.types.TransactionPayload;
import com.diem.utils.CurrencyCode;

import java.security.SecureRandom;

import static com.diem.Testnet.CHAIN_ID;

/**
 * SubmitPeerToPeerTransactionExample demonstrates currencies transfer between 2 accounts on the Libra blockchain
 */
public class SubmitPeerToPeerTransactionExample {
    public static final String CURRENCY_CODE = "XUS";

    public static void main(String[] args) throws DiemException {
        //connect to testnet
        DiemClient client = Testnet.createClient();

        //generate private key for sender account
        PrivateKey senderPrivateKey = new Ed25519PrivateKey(new Ed25519PrivateKeyParameters(new SecureRandom()));
        //generate auth key for sender account
        AuthKey senderAuthKey = AuthKey.ed24419(senderPrivateKey.publicKey());
        //create sender account with 100 XUS balance
        Testnet.mintCoins(client, 100000000, senderAuthKey.hex(), CURRENCY_CODE);

        //get sender account for sequence number
        Account account = client.getAccount(senderAuthKey.accountAddress());

        //generate private key for receiver account
        PrivateKey receiverPrivateKey = new Ed25519PrivateKey(new Ed25519PrivateKeyParameters(new SecureRandom()));
        //generate auth key for receiver account
        AuthKey receiverAuthKey = AuthKey.ed24419(receiverPrivateKey.publicKey());
        //create receiver account with 1 XUS balance
        Testnet.mintCoins(client, 10000000, receiverAuthKey.hex(), CURRENCY_CODE);

        //Create script
        TransactionPayload script = new TransactionPayload.Script(
                Helpers.encode_peer_to_peer_with_metadata_script(
                        CurrencyCode.typeTag(CURRENCY_CODE),
                        receiverAuthKey.accountAddress(),
                        10000000L,
                        new Bytes(new byte[0]),
                        new Bytes(new byte[0])));
        //Create transaction to send 1 XUS
        RawTransaction rawTransaction = new RawTransaction(
                senderAuthKey.accountAddress(),
                account.getSequenceNumber(),
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
        //Wait for the transaction to complete
        Transaction transaction = client.waitForTransaction(st, 100000);

        System.out.println(transaction);
    }
}
