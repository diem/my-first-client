import time
from random import randrange
from threading import Thread

from libra import jsonrpc
from libra.testnet import JSON_RPC_URL

from generate_keys_example import generate_auth_key
from get_account_info_example import get_account_info
from mint_example import mint

CURRENCY = "Coin1"

client = jsonrpc.Client(JSON_RPC_URL)

"""get_events_example demonstrates how to subscribe to a specific events stream base on events key"""


def main():
    # create new account
    auth_key = generate_auth_key()
    mint(auth_key.hex(), 110000000, CURRENCY)

    # get account events key
    account = get_account_info(auth_key.account_address())
    events_key = account.received_events_key

    # start minter to demonstrates events creation
    start_minter(auth_key)

    # demonstrates events subscription
    subscribe(events_key)


def subscribe_(events_key):
    start = 0

    for x in range(0, 15):
        events = client.get_events(events_key, start, 10)
        start += len(events)
        print(f"~ ${len(events)} new events found")
        time.sleep(3)

        for i in range(0, len(events)):
            print(f"~ Event # ${i + 1}:")
            print(events[i])


def minter(auth_key):
    for x in range(0, 10):
        amount = randrange(10, 19) * 10000000
        mint(auth_key.hex(), amount, CURRENCY)
        time.sleep(1)


def subscribe(events_key):
    Thread(target=subscribe_, args=(events_key,)).start()


def start_minter(auth_key):
    Thread(target=minter, args=(auth_key,)).start()


if __name__ == "__main__":
    main()
