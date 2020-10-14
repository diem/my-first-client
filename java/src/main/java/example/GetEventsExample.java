package example;

import org.libra.AuthKey;
import org.libra.LibraClient;
import org.libra.LibraException;
import org.libra.jsonrpc.LibraJsonRpcClient;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.jsonrpctypes.JsonRpc.Event;
import org.libra.utils.CurrencyCode;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.libra.Testnet.CHAIN_ID;
import static org.libra.Testnet.JSON_RPC_URL;

/**
 * GetEventsExample demonstrates how to subscribe to a specific events stream base on events key
 */
public class GetEventsExample {
    private static final LibraClient client = new LibraJsonRpcClient(JSON_RPC_URL, CHAIN_ID);

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

    public static void subscribe(String eventsKey) {
        Runnable listener = () -> {
            long start = 0;

            for (int i = 0; i < 15; i++) {
                List<Event> events;

                try {
                    events = client.getEvents(eventsKey, start, 10);
                } catch (LibraException e) {
                    throw new RuntimeException(e);
                }

                start += events.size();

                System.out.println("~ " + events.size() + " new events found");

                for (int j = 0; j < events.size(); j++) {
                    System.out.println("~ Event #" + (j + 1) + ":");
                    System.out.println(events.get(j));
                }

                try {
                    Thread.sleep(3_000);
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
                int amount = ThreadLocalRandom.current().nextInt(10, 19) * 10000000;
                MintExample.mint(authKey, String.valueOf(amount), CurrencyCode.LBR);
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread minterThread = new Thread(minter);
        minterThread.start();
    }
}
