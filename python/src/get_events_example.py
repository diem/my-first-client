import sched
import time

from libra import jsonrpc

from src.testnet import JSON_RPC_URL


class GetEventsExample:
    # Start the pooling of events for required eventsKey
    def __init__(self, events_key):
        self.events_per_account = dict()
        self.start = 0
        self.events_key = events_key
        print(f"events_key: {self.events_key}")

        if events_key not in self.events_per_account:
            self.events_per_account[events_key] = []

        self.scheduler = sched.scheduler(time.time, time.sleep)
        self.scheduler.enter(0, 1, self.event_pooling_task_example)

    # Retrieve all new events that been collected since the previous invoke
    # Reset the {newEventsPerAccount} list to avoid duplications
    def get(self, events_key):
        new_events = self.events_per_account[events_key]
        print(f"~ number of new events: {len(new_events)}")

        # "reset" new events for current event key
        self.events_per_account[events_key] = []

        return new_events

    # EventPoolingTaskExample implement the TimerTask which retrieve Event objects from LibraClient
    class EventPoolingTaskExample:
        def __init__(self, events_key):
            self.events_key = events_key
            self.client = jsonrpc.Client(JSON_RPC_URL)

        def run(self):
            self.client.get_events(self.events_key, start, 10)
