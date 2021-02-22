"""generate_keys_example demonstrates the required steps to generate keys for an account on the Libra blockchain"""

from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from diem import AuthKey, utils


def main():
    # generate private key
    private_key = Ed25519PrivateKey.generate()
    # generate auth key
    auth_key = AuthKey.from_public_key(private_key.public_key())

    print(f"Generated address: {utils.account_address_hex(auth_key.account_address())}")
    print(f"Auth Key (HEX): {auth_key.hex()}")
    print(f"Public key (HEX): {utils.public_key_bytes(private_key.public_key()).hex()}")


if __name__ == "__main__":
    main()
