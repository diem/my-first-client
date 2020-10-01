package example;

import org.libra.LibraClient;
import org.libra.LibraException;
import org.libra.jsonrpc.LibraJsonRpcClient;
import org.libra.jsonrpctypes.JsonRpc.Account;
import org.libra.jsonrpctypes.JsonRpc.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetEventsExample {
    private static Map<String, List<Event>> map = new HashMap<>();

    public static List<Event> getEvents(Account account) {
        //Connect to testnet
        LibraClient client = new LibraJsonRpcClient(Testnet.NET_URL, Testnet.CHAIN_ID);
        //Retrieve ReceivedEventsKey from account
        String receivedEventsKey = account.getReceivedEventsKey();

        if (!map.containsKey(receivedEventsKey)) {
            map.put(receivedEventsKey, new ArrayList<>());
        }

        List<Event> result = new ArrayList<>();

        try {
            List<Event> events = client.getEvents(receivedEventsKey, 0, 10);

            events.forEach(event -> {
                if (!map.get(receivedEventsKey).contains(event)) {
                    map.get(receivedEventsKey).add(event);

                    result.add(event);
                }
            });

            System.out.println("~ number of events: " + events.size());
            System.out.println("~ number of new events: " + result.size());
        } catch (LibraException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
