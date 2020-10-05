package example;

import org.libra.LibraClient;
import org.libra.LibraException;
import org.libra.jsonrpc.LibraJsonRpcClient;
import org.libra.jsonrpctypes.JsonRpc.Event;

import java.util.*;

public class GetEventsExample {
    private final Map<String, Queue<Event>> newEventsPerAccount = new HashMap<>();
    private final Timer timer = new Timer();
    private long start = 0;

    /**
     * Start the pooling of events for required eventsKey
     *
     * @param eventsKey
     */
    public void start(String eventsKey) {
        System.out.println("receivedEventsKey: " + eventsKey);

        if (!newEventsPerAccount.containsKey(eventsKey)) {
            newEventsPerAccount.put(eventsKey, new LinkedList<>());
        }

        timer.scheduleAtFixedRate(new EventPoolingTaskExample(eventsKey), 0, 1000);
    }

    /**
     * Retrieve all new events that been collected since the previous invoke
     * Reset the {newEventsPerAccount} list to avoid duplications
     *
     * @param eventsKey
     * @return newEvents
     */
    public Queue<Event> get(String eventsKey) {
        Queue<Event> newEvents = newEventsPerAccount.get(eventsKey);
        System.out.println("~ number of new events: " + newEvents.size());

        //"reset" new events for current event key
        newEventsPerAccount.put(eventsKey, new LinkedList<>());

        return newEvents;
    }

    /**
     * EventPoolingTaskExample implement the TimerTask which retrieve Event objects from LibraClient
     */
    private class EventPoolingTaskExample extends TimerTask {
        private final String eventsKey;
        private final LibraClient client;

        public EventPoolingTaskExample(String eventsKey) {
            this.eventsKey = eventsKey;
            //Connect to testnet
            client = new LibraJsonRpcClient(Testnet.NET_URL, Testnet.CHAIN_ID);
        }

        public void run() {
            try {
                List<Event> events = client.getEvents(eventsKey, start, 10);

                events.forEach(event -> {
                    if (!newEventsPerAccount.get(eventsKey).contains(event)) {
                        start = event.getSequenceNumber() + 1;
                        newEventsPerAccount.get(eventsKey).add(event);
                    }
                });
            } catch (LibraException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stop() {
        timer.cancel();
    }
}
