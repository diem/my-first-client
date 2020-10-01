import com.novi.serde.Bytes;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

import static org.libra.Testnet.FAUCET_SERVER_URL;

public class Main {
    public static String NET_URL = "https://testnet.libra.org/v1";
    public static ChainId CHAIN_ID = new ChainId((byte) 2);

    /**
     * This code demonstrates basic operations to work with the LibraClient.
     * 1. Connect to testnet
     * 2. Create account
     * 3. Get account information
     * 4. Add money to existing account
     * 5. Transfer money between accounts (peer to peer transaction)
     * 6. Follow transaction status
     */
    public static void howToWorkWithTestnet() {
        //Connect to testnet
        LibraClient client = new LibraJsonRpcClient(NET_URL, CHAIN_ID);

        //Create new account (mint to new address)
        PrivateKey privateKey = GenerateKeys.generatePrivateKey();
        AuthKey authKey = GenerateKeys.generateAuthKey(privateKey);

        Minter.mint(authKey, "1340000000", CurrencyCode.LBR);

        String accountAddress = GenerateKeys.getGeneratedAddress(authKey);

        //Get account information
        Account account = GetAccountInfo.getAccountInfo(client, accountAddress);

        //Add money to account
        Minter.mint(authKey, "270000000", CurrencyCode.LBR);

        //Peer 2 peer transaction
        //Create second account
        PrivateKey receiverPrivateKey = GenerateKeys.generatePrivateKey();
        AuthKey receiverAuthKey = GenerateKeys.generateAuthKey(receiverPrivateKey);

        Minter.mint(receiverAuthKey, "2560000000", CurrencyCode.LBR);

        //Only to display receiver address
        PeerToPeerTransactionSubmit.submitPeerToPeerTransaction(client, privateKey, authKey, account, receiverAuthKey);
    }
}
