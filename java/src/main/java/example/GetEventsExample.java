package example;

import org.libra.LibraClient;
import org.libra.LibraException;
import org.libra.jsonrpc.LibraJsonRpcClient;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.jsonrpctypes.JsonRpc.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GetEventsExample {
    private static final Map<String, List<Event>> eventsPerAccount = new ConcurrentHashMap<>();
    private static final Map<String, List<Event>> newEventsPerAccount = new ConcurrentHashMap<>();

    public static List<Event> getEvents(Account account) {
        //Retrieve ReceivedEventsKey from account
        String receivedEventsKey = account.getReceivedEventsKey();

        if (!eventsPerAccount.containsKey(receivedEventsKey)) {
            eventsPerAccount.put(receivedEventsKey, new ArrayList<>());
            newEventsPerAccount.put(receivedEventsKey, new ArrayList<>());
        }

        List<Event> events = collectNewEvents(receivedEventsKey);

        newEventsPerAccount.put(receivedEventsKey, new ArrayList<>());

        return events;
    }

    private static List<Event> collectNewEvents(String eventsKey) {
        //Connect to testnet
        LibraClient client = new LibraJsonRpcClient(Testnet.NET_URL, Testnet.CHAIN_ID);

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

        System.out.println("~ number of events: " + eventsPerAccount.get(eventsKey).size());
        List<Event> events = newEventsPerAccount.get(eventsKey);
        System.out.println("~ number of new events: " + events.size());
        return events;
    }
}
