package example;

import org.libra.LibraClient;
import org.libra.LibraException;
import org.libra.jsonrpc.LibraJsonRpcClient;
import org.libra.jsonrpctypes.JsonRpc.Event;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GetEventsExample {
    private List<Event> newEvents = new LinkedList<>();
    private final Timer timer = new Timer();
    private long start = 0;

    /**
     * Start the pooling of events for required eventsKey
     */
    public GetEventsExample(String eventsKey) {
        System.out.println("receivedEventsKey: " + eventsKey);

        timer.scheduleAtFixedRate(new EventPoolingTaskExample(eventsKey), 0, 1000);
    }

    /**
     * Retrieve all new events that been collected since the previous invoke
     * Reset the {newEventsPerAccount} list to avoid duplications
     *
     * @return newEvents
     */
    public List<Event> get() {
        System.out.println("~ number of new events: " + newEvents.size());

        List<Event> newEventsCopy = new LinkedList<>(newEvents);

        newEvents = new LinkedList<>();

        return newEventsCopy;
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
                newEvents.addAll(events);
                events.forEach(event -> start = event.getSequenceNumber() + 1
                );
            } catch (LibraException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stop() {
        timer.cancel();
    }
}
