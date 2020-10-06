from datetime import timedelta

from libra import jsonrpc
from timeloop import Timeloop
from timeloop.job import Job

from src.testnet import JSON_RPC_URL


class GetEventsExample:
    # Start the pooling of events for required eventsKey
    def __init__(self, events_key):
        print("init GetEventsExample")
        self.events_key = events_key
        self.new_events = []
        self.start = 0
        print(f"events_key: {self.events_key}")
        self.tl = Timeloop()
        self.tl.start()
        self.tl.job(Job(interval=timedelta(seconds=30), execute=pool_new_events(self)))
        print("finish init GetEventsExample")

    # Reset the {newEventsPerAccount} list to avoid duplications
    def get(self):
        new_events_copy = self.new_events
        print(f"~ number of new events: {len(new_events_copy)}")

        # "reset" new events for current event key
        self.new_events = []

        return new_events_copy

    def stop(self):
        self.tl.stop()


def pool_new_events(events_example: GetEventsExample):
    print("run pool_new_events")
    client = jsonrpc.Client(JSON_RPC_URL)
    events = client.get_events(events_example.events_key, events_example.start, 10)
    events_example.new_events.append(events)
    print("finish pool_new_events")
