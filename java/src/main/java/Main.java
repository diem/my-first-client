import org.libra.AuthKey;
import org.libra.PrivateKey;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.types.ChainId;
import org.libra.utils.CurrencyCode;

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
    public static void main(String args[]) {
        //Create new account (mint to new address)
        PrivateKey privateKey = GenerateKeys.generatePrivateKey();
        AuthKey authKey = GenerateKeys.generateAuthKey(privateKey);

        Mint.mint(authKey, "1340000000", CurrencyCode.LBR);

        String accountAddress = GenerateKeys.getGeneratedAddress(authKey);

        //Get account information
        Account account = GetAccountInfo.getAccountInfo(accountAddress);

        //Add money to account
        Mint.mint(authKey, "270000000", CurrencyCode.LBR);

        //Peer 2 peer transaction
        //Create second account
        PrivateKey receiverPrivateKey = GenerateKeys.generatePrivateKey();
        AuthKey receiverAuthKey = GenerateKeys.generateAuthKey(receiverPrivateKey);

        Mint.mint(receiverAuthKey, "2560000000", CurrencyCode.LBR);

        //Only to display receiver address
        SubmitPeerToPeerTransaction.submitPeerToPeerTransaction(privateKey, authKey, account, receiverAuthKey);
    }
}
