package example;

import com.novi.serde.Bytes;
import org.libra.LibraClient;
import org.libra.LibraException;
import org.libra.PrivateKey;
import org.libra.Signer;
import org.libra.jsonrpc.LibraJsonRpcClient;
import org.libra.jsonrpctypes.JsonRpc.Transaction;
import org.libra.stdlib.Helpers;
import org.libra.types.AccountAddress;
import org.libra.types.RawTransaction;
import org.libra.types.SignedTransaction;
import org.libra.types.TransactionPayload;
import org.libra.utils.CurrencyCode;

public class SubmitPeerToPeerTransactionExample {
    public static void submitPeerToPeerTransaction(PrivateKey privateKey,
                                                   long amount,
                                                   AccountAddress receiverAccountAddress,
                                                   AccountAddress senderAccountAddress,
                                                   long sequenceNumber,
                                                   String currencyCode) {
        //Connect to testnet
        LibraClient client = new LibraJsonRpcClient(Testnet.NET_URL, Testnet.CHAIN_ID);
        //Create script
        TransactionPayload script = generateScript(receiverAccountAddress, amount, currencyCode);
        //Create transaction
        RawTransaction rawTransaction = generateRawTransaction(script, senderAccountAddress, sequenceNumber, 1000000L, 0L, 100000000000L);
        //Sign transaction
        SignedTransaction st = Signer.sign(privateKey, rawTransaction);
        //Submit transaction
        try {
            submitTransaction(client, st);
        } catch (LibraException e) {
            throw new RuntimeException("Failed to submit transaction", e);
        }
        //Wait for the transaction to complete
        try {
            waitForTransaction(client, st);
        } catch (LibraException e) {
            throw new RuntimeException("Failed while waiting to transaction ", e);
        }
    }

    private static void waitForTransaction(LibraClient client, SignedTransaction st) throws LibraException {
        Transaction transaction = client.waitForTransaction(st, 100000);

        System.out.println(transaction);
    }

    private static void submitTransaction(LibraClient client, SignedTransaction st) throws LibraException {
        client.submit(st);
    }

    private static RawTransaction generateRawTransaction(TransactionPayload script,
                                                         AccountAddress senderAccountAddress,
                                                         long sequenceNumber, long maxGasAmount,
                                                         long gasUnitPrice, long expirationTimestampSecs) {
        return new RawTransaction(
                senderAccountAddress,
                sequenceNumber,
                script,
                maxGasAmount,
                gasUnitPrice,
                CurrencyCode.LBR,
                expirationTimestampSecs,
                Testnet.CHAIN_ID);
    }

    private static TransactionPayload generateScript(AccountAddress receiverAccountAddress,
                                                     long amount, String currencyCode) {
        return new TransactionPayload.Script(
                Helpers.encode_peer_to_peer_with_metadata_script(
                        CurrencyCode.typeTag(currencyCode),
                        receiverAccountAddress,
                        amount,
                        new Bytes(new byte[0]),
                        new Bytes(new byte[0])));
    }
}
