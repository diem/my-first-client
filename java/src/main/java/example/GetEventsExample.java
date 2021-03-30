package example;

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import com.diem.*;
import com.diem.jsonrpc.JsonRpc.Account;
import com.diem.jsonrpc.JsonRpc.Event;

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * GetEventsExample demonstrates how to subscribe to a specific events stream base on events key
 */
public class GetEventsExample {
    public static final String CURRENCY_CODE = "XUS";

    public static void main(String[] args) throws DiemException {
        //connect to testnet
        DiemClient client = Testnet.createClient();

        //create new account
        SecureRandom random = new SecureRandom();
        Ed25519PrivateKeyParameters privateKeyParams = new Ed25519PrivateKeyParameters(random);
        Ed25519PrivateKey privateKey = new Ed25519PrivateKey(privateKeyParams);
        AuthKey authKey = AuthKey.ed25519(privateKey.publicKey());
        Testnet.mintCoins(client, 100000000, authKey.hex(), CURRENCY_CODE);

        //get account events key
        Account account = client.getAccount(authKey.accountAddress());
        String eventsKey = account.getReceivedEventsKey();

        //start minter to demonstrates events creation
        startMinter(client, authKey);

        //demonstrates events subscription
        subscribe(client, eventsKey);
    }

    public static void subscribe(DiemClient client, String eventsKey) {
        Runnable listener = () -> {
            long start = 0;

            for (int i = 0; i < 15; i++) {
                List<Event> events;

                try {
                    events = client.getEvents(eventsKey, start, 10);
                } catch (DiemException e) {
                    throw new RuntimeException(e);
                }

                start += events.size();

                System.out.println(events.size() + " new events found");

                for (int j = 0; j < events.size(); j++) {
                    System.out.println("Event #" + (j + 1) + ":");
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

    private static void startMinter(DiemClient client, AuthKey authKey) {
        Runnable minter = () -> {
            for (int i = 0; i < 10; i++) {
                int amount =  1000000;
                Testnet.mintCoins(client, amount, authKey.hex(), CURRENCY_CODE);
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
