from libra import jsonrpc
from libra.jsonrpc import Account

from src.generate_keys_example import extract_account_address, generate_auth_key
from src.mint_example import mint
from src.testnet import JSON_RPC_URL

"""
get_account_info_example demonstrates the required operation to retrieve account information from the Libra blockchain
"""


def main():
    # create account
    auth_key = generate_auth_key()
    mint(auth_key.hex(), 1340000000, "LBR")
    account_address = extract_account_address(auth_key)

    # get account information
    account = get_account_info(account_address)

    print(account)


def get_account_info(account_address: str) -> Account:
    client = jsonrpc.Client(JSON_RPC_URL)
    return client.get_account(account_address)


if __name__ == "__main__":
    main()
