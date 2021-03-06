"""
get_account_info_example demonstrates the required operation to retrieve account information from the Libra blockchain
"""

from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from diem import testnet, AuthKey, utils


def main():
    # connect to testnet
    client = testnet.create_client()

    # generate private key
    private_key = Ed25519PrivateKey.generate()
    # generate auth key
    auth_key = AuthKey.from_public_key(private_key.public_key())
    print(f"Generated address: {utils.account_address_hex(auth_key.account_address())}")

    # create account
    faucet = testnet.Faucet(client)
    testnet.Faucet.mint(faucet, auth_key.hex(), 100_000_000, "XUS")

    # get account information
    account = client.get_account(auth_key.account_address())

    print("Account info:")
    print(account)


if __name__ == "__main__":
    main()
