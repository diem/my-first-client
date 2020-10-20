package example;

import org.libra.AuthKey;
import org.libra.LibraClient;
import org.libra.LibraException;
import org.libra.Testnet;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.jsonrpctypes.JsonRpc.Event;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * GetEventsExample demonstrates how to subscribe to a specific events stream base on events key
 */
public class GetEventsExample {
    public static final String CURRENCY_CODE = "Coin1";

    public static void main(String[] args) {
        //connect to testnet
        LibraClient client = Testnet.createClient();

        //create new account
        AuthKey authKey = GenerateKeysExample.generateAuthKey();
        MintExample.mint(client, authKey, 110000000, CURRENCY_CODE);

        //get account events key
        Account account = GetAccountInfoExample.getAccountInfo(client, authKey.accountAddress());
        String eventsKey = account.getReceivedEventsKey();

        //start minter to demonstrates events creation
        startMinter(client, authKey);

        //demonstrates events subscription
        subscribe(client, eventsKey);
    }

    public static void subscribe(LibraClient client, String eventsKey) {
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

    private static void startMinter(LibraClient client, AuthKey authKey) {
        Runnable minter = () -> {
            for (int i = 0; i < 10; i++) {
                int amount = ThreadLocalRandom.current().nextInt(10, 19) * 10000000;
                MintExample.mint(client, authKey, amount, CURRENCY_CODE);
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
