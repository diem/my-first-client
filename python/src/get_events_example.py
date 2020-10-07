import time
from random import randrange
from threading import Thread

from libra import jsonrpc

from src.generate_keys_example import generate_auth_key_, extract_account_address
from src.get_account_info_example import get_account_info
from src.mint_example import mint
from src.testnet import JSON_RPC_URL

client = jsonrpc.Client(JSON_RPC_URL)

"""get_events_example demonstrates how to subscribe to a specific events stream base on events key"""


def main():
    # create new account
    auth_key = generate_auth_key_()
    mint(auth_key.hex(), 110000000, "LBR")
    account_address = extract_account_address(auth_key)

    # get account events key
    account = get_account_info(account_address)
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
        print(events)
        time.sleep(3)


def minter(auth_key):
    for x in range(0, 10):
        amount = randrange(10, 19) * 10000000
        mint(auth_key.hex(), amount, "LBR")
        time.sleep(1)


def subscribe(events_key):
    Thread(target=subscribe_, args=(events_key,)).start()


def start_minter(auth_key):
    Thread(target=minter, args=(auth_key,)).start()


if __name__ == "__main__":
    main()
