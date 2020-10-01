import com.novi.serde.Bytes;
import org.libra.*;
import org.libra.jsonrpctypes.JsonRpc;
import org.libra.stdlib.Helpers;
import org.libra.types.RawTransaction;
import org.libra.types.SignedTransaction;
import org.libra.types.TransactionPayload;
import org.libra.utils.CurrencyCode;

import static org.libra.Testnet.CHAIN_ID;

public class SubmitPeerToPeerTransaction {
    public static void submitPeerToPeerTransaction(LibraClient client, PrivateKey privateKey, AuthKey authKey, JsonRpc.Account account, AuthKey receiverAuthKey) {
        //Create script
        TransactionPayload script = generateScript(receiverAuthKey);
        //Create transaction
        RawTransaction rawTransaction = generateRawTransaction(authKey, account, script);
        //Sign transaction
        SignedTransaction st = Signer.sign(privateKey, rawTransaction);
        //Submit transaction
        submitTransaction(client, st);
        //Wait for the transaction to complete
        waitForTransaction(client, st);
    }

    private static void waitForTransaction(LibraClient client, SignedTransaction st) {
        try {
            JsonRpc.Transaction transaction = client.waitForTransaction(st, 100000);

            System.out.println(transaction);
        } catch (LibraException e) {
            throw new RuntimeException(e);
        }
    }

    private static void submitTransaction(LibraClient client, SignedTransaction st) {
        try {
            client.submit(st);
        } catch (LibraException e) {
            throw new RuntimeException(e);
        }
    }

    private static RawTransaction generateRawTransaction(AuthKey authKey, JsonRpc.Account account, TransactionPayload script) {
        RawTransaction rawTransaction = new RawTransaction(
                authKey.accountAddress(),
                account.getSequenceNumber(), script,
                1000000L, 0L,
                CurrencyCode.LBR, 100000000000L,
                CHAIN_ID);
        return rawTransaction;
    }

    private static TransactionPayload generateScript(AuthKey receiverAuthKey) {
        TransactionPayload script = new TransactionPayload.Script(
                Helpers.encode_peer_to_peer_with_metadata_script(
                        CurrencyCode.typeTag(CurrencyCode.LBR),
                        receiverAuthKey.accountAddress(),
                        1120000000L,
                        new Bytes(new byte[0]),
                        new Bytes(new byte[0])));
        return script;
    }
}
