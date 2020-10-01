import example.GenerateKeysExample;
import example.GetAccountInfoExample;
import example.MintExample;
import example.SubmitPeerToPeerTransactionExample;
import org.libra.AuthKey;
import org.libra.PrivateKey;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.utils.CurrencyCode;

public class Main {
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
        PrivateKey privateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey authKey = GenerateKeysExample.generateAuthKey(privateKey);

        MintExample.mint(authKey, "1340000000", CurrencyCode.LBR);

        String accountAddress = GenerateKeysExample.getGeneratedAddress(authKey);

        //Get account information
        Account account = GetAccountInfoExample.getAccountInfo(accountAddress);

        //Add money to account
        MintExample.mint(authKey, "270000000", CurrencyCode.LBR);

        //Peer 2 peer transaction
        //Create second account
        PrivateKey receiverPrivateKey = GenerateKeysExample.generatePrivateKey();
        AuthKey receiverAuthKey = GenerateKeysExample.generateAuthKey(receiverPrivateKey);

        MintExample.mint(receiverAuthKey, "2560000000", CurrencyCode.LBR);

        //Only to display receiver address
        SubmitPeerToPeerTransactionExample.submitPeerToPeerTransaction(privateKey, authKey, account, receiverAuthKey);
    }
}
