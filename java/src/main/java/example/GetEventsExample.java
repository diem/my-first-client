package example;

import org.libra.LibraClient;
import org.libra.LibraException;
import org.libra.jsonrpc.LibraJsonRpcClient;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.jsonrpctypes.JsonRpc.Event;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GetEventsExample {
    private static final Map<String, List<Event>> eventsPerAccount = new ConcurrentHashMap<>();
    private static final Map<String, List<Event>> newEventsPerAccount = new ConcurrentHashMap<>();
    private static final Timer timer = new Timer();

    public static void start(Account account) {
        String receivedEventsKey = account.getReceivedEventsKey();

        if (!eventsPerAccount.containsKey(receivedEventsKey)) {
            eventsPerAccount.put(receivedEventsKey, new ArrayList<>());
            newEventsPerAccount.put(receivedEventsKey, new ArrayList<>());
        }

        timer.scheduleAtFixedRate(new GetNewEventsFromClientExample(account.getReceivedEventsKey()), 0L, 1000L);
    }

    public static List<Event> get(Account account) {
        List<Event> allEvents = eventsPerAccount.get(account.getReceivedEventsKey());
        System.out.println("~ number of events: " + allEvents.size());

        List<Event> newEvents = newEventsPerAccount.get(account.getReceivedEventsKey());
        System.out.println("~ number of new events: " + newEvents.size());

        //"reset" new events for current account
        newEventsPerAccount.put(account.getReceivedEventsKey(), new ArrayList<>());

        return newEvents;
    }

    private static class GetNewEventsFromClientExample extends TimerTask {
        private final String eventsKey;
        LibraClient client;

        public GetNewEventsFromClientExample(String eventsKey) {
            this.eventsKey = eventsKey;
            //Connect to testnet
            client = new LibraJsonRpcClient(Testnet.NET_URL, Testnet.CHAIN_ID);
        }

        public void run() {
            try {
                List<Event> events = client.getEvents(eventsKey, 0, 10);

                events.forEach(event -> {
                    if (!eventsPerAccount.get(eventsKey).contains(event)) {
                        eventsPerAccount.get(eventsKey).add(event);
                        newEventsPerAccount.get(eventsKey).add(event);
                    }
                });
            } catch (LibraException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void stop() {
        timer.cancel();
    }
}
