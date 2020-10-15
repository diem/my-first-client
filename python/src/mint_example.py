from libra import jsonrpc, testnet
from libra.testnet import Faucet, JSON_RPC_URL

from generate_keys_example import generate_auth_key

"""
 mint_example demonstrates how to add currencies to account on the Libra blockchain
 The mint also use to create new account by adding currencies base on new auth_key
"""


def main():
    auth_key = generate_auth_key()

    # use mint to create new account
    mint(auth_key.hex(), 192000000, "Coin1")


def mint(auth_key: str, amount: int, currency_code: str) -> None:
    client = jsonrpc.Client(JSON_RPC_URL)
    faucet = testnet.Faucet(client)

    return Faucet.mint(faucet, auth_key, amount, currency_code)


if __name__ == "__main__":
    main()
