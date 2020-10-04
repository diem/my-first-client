package example;

import org.libra.LibraClient;
import org.libra.LibraException;
import org.libra.jsonrpc.LibraJsonRpcClient;
import org.libra.jsonrpctypes.JsonRpc.Event;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class GetEventsExample {
    private static final Map<String, Queue<EventExample>> eventsPerAccount = new ConcurrentHashMap<>();
    private static final Map<String, Queue<EventExample>> newEventsPerAccount = new ConcurrentHashMap<>();
    private static final Timer timer = new Timer();

    public static void start(String eventsKey) {
        System.out.println("receivedEventsKey: " + eventsKey);

        if (!eventsPerAccount.containsKey(eventsKey)) {
            eventsPerAccount.put(eventsKey, new PriorityBlockingQueue<>());
            newEventsPerAccount.put(eventsKey, new PriorityBlockingQueue<>());
        }

        timer.scheduleAtFixedRate(new GetNewEventsFromClientExample(eventsKey), 0, 1000);
    }

    public static Queue<EventExample> get(String eventsKey) {
        Queue<EventExample> allEvents = eventsPerAccount.get(eventsKey);
        System.out.println("~ number of events: " + allEvents.size());

        Queue<EventExample> newEvents = newEventsPerAccount.get(eventsKey);
        System.out.println("~ number of new events: " + newEvents.size());

        //"reset" new events for current event key
        newEventsPerAccount.put(eventsKey, new PriorityBlockingQueue<>());

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

                List<EventExample> eventExamples = new ArrayList<>();

                events.forEach(event -> {
                    EventExample eventExample = new EventExample(event);
                    eventExamples.add(eventExample);
                });

                eventExamples.forEach(eventExample -> {
                    if (!eventsPerAccount.get(eventsKey).contains(eventExample)) {
                        eventsPerAccount.get(eventsKey).add(eventExample);
                        newEventsPerAccount.get(eventsKey).add(eventExample);
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

    public static class EventExample implements Comparable<EventExample> {
        private final Event event;

        public EventExample(Event event) {
            this.event = event;
        }

        @Override
        public boolean equals(Object o) {
            return this.event.equals(((EventExample) o).event);
        }

        @Override
        public int compareTo(EventExample event) {
            return Long.compare(this.event.getSequenceNumber(), event.event.getSequenceNumber());

        }
    }
}