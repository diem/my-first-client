import org.libra.LibraClient;
import org.libra.LibraException;
import org.libra.jsonrpctypes.JsonRpc.Account;

public class GetAccountInfo {
    public static Account getAccountInfo(LibraClient client, String accountAddress) {
        Account account;

        try {
            account = client.getAccount(accountAddress);

            System.out.println(account);
        } catch (LibraException e) {
            throw new RuntimeException(e);
        }

        return account;
    }
}
