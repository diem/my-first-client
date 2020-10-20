from libra import testnet

import generate_keys_example

"""
 mint_example demonstrates how to add currencies to account on the Libra blockchain testnet
 The mint also use to create new account by adding currencies base on new auth_key
"""


def main():
    # connect to testnet
    client = testnet.create_client()

    auth_key = generate_keys_example.generate_auth_key()

    # use mint to create new account
    mint(client, auth_key.hex(), 192000000, "Coin1")


def mint(client, auth_key: str, amount: int, currency_code: str) -> None:
    faucet = testnet.Faucet(client)

    return testnet.Faucet.mint(faucet, auth_key, amount, currency_code)


if __name__ == "__main__":
    main()
