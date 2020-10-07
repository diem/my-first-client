package example;

import org.libra.AuthKey;
import org.libra.LibraClient;
import org.libra.LibraException;
import org.libra.jsonrpc.LibraJsonRpcClient;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.jsonrpctypes.JsonRpc.Event;
import org.libra.utils.CurrencyCode;

import java.util.List;

public class GetEventsExample {
    private static final LibraClient client = new LibraJsonRpcClient(Testnet.NET_URL, Testnet.CHAIN_ID);
    private static long start = 0;

    public static void main(String[] args) {
        //create new account
        AuthKey authKey = GenerateKeysExample.generateAuthKey();
        MintExample.mint(authKey, "110000000", CurrencyCode.LBR);
        String accountAddress = GenerateKeysExample.extractAccountAddress(authKey);
        //get account events key
        Account account = GetAccountInfoExample.getAccountInfo(accountAddress);
        String eventsKey = account.getReceivedEventsKey();

        //start minter to demonstrates events creation
        startMinter(authKey);

        //demonstrates events subscription
        subscribe(eventsKey);
    }

    private static List<Event> getEventsByKey(String eventsKey) {
        try {
            return client.getEvents(eventsKey, start, 10);
        } catch (LibraException e) {
            throw new RuntimeException(e);
        }
    }

    public static void subscribe(String eventsKey) {
        Runnable listener = () -> {
            for (int i = 0; i < 15; i++) {
                List<Event> events = getEventsByKey(eventsKey);

                start += events.size();
                System.out.println(events);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread listenerThread = new Thread(listener);
        listenerThread.start();
    }

    private static void startMinter(AuthKey authKey) {
        Runnable minter = () -> {
            for (int i = 0; i < 10; i++) {
                MintExample.mint(authKey, "110000000", CurrencyCode.LBR);
            }
        };

        Thread minterThread = new Thread(minter);
        minterThread.start();
    }
}
