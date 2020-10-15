from libra import jsonrpc
from libra.jsonrpc import Account
from libra.libra_types import AccountAddress
from libra.testnet import JSON_RPC_URL

from generate_keys_example import generate_auth_key
from mint_example import mint

"""
get_account_info_example demonstrates the required operation to retrieve account information from the Libra blockchain
"""


def main():
    # create account
    auth_key = generate_auth_key()
    mint(auth_key.hex(), 1340000000, "Coin1")

    # get account information
    account = get_account_info(auth_key.account_address())

    print("~ Account info:")
    print(account)


def get_account_info(account_address: AccountAddress) -> Account:
    client = jsonrpc.Client(JSON_RPC_URL)

    return client.get_account(account_address)


if __name__ == "__main__":
    main()
