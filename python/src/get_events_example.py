import time
from random import randrange
from threading import Thread

from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from libra import testnet, AuthKey, utils

CURRENCY = "Coin1"

"""get_events_example demonstrates how to subscribe to a specific events stream base on events key"""


def main():
    # connect to testnet
    client = testnet.create_client()

    # generate private key
    private_key = Ed25519PrivateKey.generate()
    # generate auth key
    auth_key = AuthKey.from_public_key(private_key.public_key())
    print(f"~ Generated address: {utils.account_address_hex(auth_key.account_address())}")
    # create new account
    faucet = testnet.Faucet(client)
    testnet.Faucet.mint(faucet, auth_key.hex(), 100000000, CURRENCY)

    # get account events key
    account = client.get_account(auth_key.account_address())
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
        amount = 1000000
        faucet = testnet.Faucet(client)
        testnet.Faucet.mint(faucet, auth_key.hex(), amount, CURRENCY)
        time.sleep(1)


def subscribe(client, events_key):
    Thread(target=subscribe_, args=(client, events_key,)).start()


def start_minter(client, auth_key):
    Thread(target=minter, args=(client, auth_key)).start()


if __name__ == "__main__":
    main()
