"""
 mint_example demonstrates how to add currencies to account on the Libra blockchain testnet
 The mint also use to create new account by adding currencies base on new auth_key
"""

from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from diem import testnet, AuthKey, utils


def main():
    # connect to testnet
    client = testnet.create_client()

    # generate private key for new account
    private_key = Ed25519PrivateKey.generate()

    # generate auth key for new account
    auth_key = AuthKey.from_public_key(private_key.public_key())
    print(f"Generated address: {utils.account_address_hex(auth_key.account_address())}")

    # use mint to create new account
    faucet = testnet.Faucet(client)
    testnet.Faucet.mint(faucet, auth_key.hex(), 192000000, "XUS")


if __name__ == "__main__":
    main()
