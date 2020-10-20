import time
from random import randrange
from threading import Thread

from libra import testnet

import generate_keys_example
import get_account_example
import mint_example

CURRENCY = "Coin1"

"""get_events_example demonstrates how to subscribe to a specific events stream base on events key"""


def main():
    # connect to testnet
    client = testnet.create_client()

    # create new account
    auth_key = generate_keys_example.generate_auth_key()
    mint_example.mint(auth_key.hex(), 110000000, CURRENCY)

    # get account events key
    account = get_account_example.get_account(client, auth_key.account_address())
    events_key = account.received_events_key

    # start minter to demonstrates events creation
    start_minter(client, auth_key)

    # demonstrates events subscription
    subscribe(client, events_key)


def subscribe_(client, events_key):
    start = 0

    for x in range(0, 15):
        events = client.get_events(events_key, start, 10)
        start += len(events)
        print(f"~ {len(events)} new events found")
        time.sleep(3)

        for i in range(0, len(events)):
            print(f"~ Event # {i + 1}:")
            print(events[i])


def minter(client, auth_key):
    for x in range(0, 10):
        amount = randrange(10, 19) * 10000000
        mint_example.mint(client, auth_key.hex(), amount, CURRENCY)
        time.sleep(1)


def subscribe(client, events_key):
    Thread(target=subscribe_, args=(client, events_key,)).start()


def start_minter(client, auth_key):
    Thread(target=minter, args=(client, auth_key)).start()


if __name__ == "__main__":
    main()
