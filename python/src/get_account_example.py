from libra import testnet
from libra.jsonrpc import Account
from libra.libra_types import AccountAddress

import generate_keys_example
import mint_example

"""
get_account_info_example demonstrates the required operation to retrieve account information from the Libra blockchain
"""


def main():
    # connect to testnet
    client = testnet.create_client()

    # create account
    auth_key = generate_keys_example.generate_auth_key()
    mint_example.mint(auth_key.hex(), 1340000000, "Coin1")

    # get account information
    account = get_account(client, auth_key.account_address())

    print("~ Account info:")
    print(account)


def get_account(client, account_address: AccountAddress) -> Account:
    return client.get_account(account_address)


if __name__ == "__main__":
    main()
