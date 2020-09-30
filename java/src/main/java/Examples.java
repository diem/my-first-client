import com.novi.serde.Bytes;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.libra.*;
import org.libra.jsonrpc.InvalidResponseException;
import org.libra.jsonrpc.LibraJsonRpcClient;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.jsonrpctypes.JsonRpc.Transaction;
import org.libra.stdlib.Helpers;
import org.libra.types.ChainId;
import org.libra.types.RawTransaction;
import org.libra.types.SignedTransaction;
import org.libra.types.TransactionPayload;
import org.libra.utils.CurrencyCode;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;

import static org.libra.Testnet.FAUCET_SERVER_URL;

public class Examples {
    public static String NET_URL = "https://testnet.libra.org/v1";
    public static ChainId CHAIN_ID = new ChainId((byte) 2);
    private static LibraClient client;

    public static void howToWorkWithTestnet() {
        //connect to testnet
        client = new LibraJsonRpcClient(NET_URL, CHAIN_ID);

        //create new account (mint to new address)
        SecureRandom random = new SecureRandom();
        Ed25519PrivateKeyParameters privateKeyParams = new Ed25519PrivateKeyParameters(random);
        PrivateKey privateKey = new Ed25519PrivateKey(privateKeyParams);
        AuthKey authKey = AuthKey.ed24419(privateKey.publicKey());

        mint(authKey, "1340000000", CurrencyCode.LBR);

        String accountAddress = authKey.hex().toLowerCase().substring(32, authKey.hex().length());
        System.out.println("address: " + accountAddress);

        //get account info
        Account account;
        try {
            account = client.getAccount(accountAddress);

            System.out.println(account);
        } catch (LibraException e) {
            throw new RuntimeException(e);
        }

        //add money to account
        mint(authKey, "270000000", CurrencyCode.LBR);

        //peer 2 peer transaction
        //create receiver account
        SecureRandom receiverRandom = new SecureRandom();
        Ed25519PrivateKeyParameters receiverPrivateKeyParams = new Ed25519PrivateKeyParameters(receiverRandom);
        PrivateKey receiverPrivateKey = new Ed25519PrivateKey(receiverPrivateKeyParams);
        AuthKey receiverAuthKey = AuthKey.ed24419(receiverPrivateKey.publicKey());

        mint(receiverAuthKey, "2560000000", CurrencyCode.LBR);

        String receiverAccountAddress = receiverAuthKey.hex().toLowerCase().substring(32, receiverAuthKey.hex().length());
        System.out.println("receiver address: " + receiverAccountAddress);
        //create script
        TransactionPayload script = new TransactionPayload.Script(
                Helpers.encode_peer_to_peer_with_metadata_script(
                        CurrencyCode.typeTag(CurrencyCode.LBR),
                        receiverAuthKey.accountAddress(),
                        1120000000L,
                        new Bytes(new byte[0]),
                        new Bytes(new byte[0])));
        //create transaction
        RawTransaction rawTransaction = new RawTransaction(
                authKey.accountAddress(),
                account.getSequenceNumber(), script,
                1000000L, 0L,
                CurrencyCode.LBR, 100000000000L,
                CHAIN_ID);
        //sign transaction
        SignedTransaction st = Signer.sign(privateKey,
                rawTransaction);
        //submit transaction
        try {
            client.submit(st);
        } catch (LibraException e) {
            throw new RuntimeException(e);
        }

        //wait for transaction to be finish
        try {
            Transaction transaction = client.waitForTransaction(st, 100000);

            System.out.println(transaction);
        } catch (LibraException e) {
            throw new RuntimeException(e);
        }
    }

    private static void mint(AuthKey authKey, String amount, String currencyCode) {
        URI build;

        try {
            URIBuilder builder = new URIBuilder(FAUCET_SERVER_URL);
            builder.setParameter("amount", amount).setParameter("auth_key", authKey.hex().toLowerCase())
                    .setParameter("currency_code", currencyCode);
            build = builder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        System.out.println(build);

        HttpPost post = new HttpPost(build);
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpResponse response = httpClient.execute(post);
            String body = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new InvalidResponseException(response.getStatusLine().getStatusCode(), body);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
