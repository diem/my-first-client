from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from libra import AuthKey, utils

"""generate_keys_example demonstrates the required steps to generate keys for an account on the Libra blockchain"""


def main():
    private_key = generate_private_key()

    auth_key = generate_auth_key(private_key)

    print(f"~ Auth Key (HEX): ${auth_key.hex()}")
    print(f"~ Public key (HEX): ${utils.public_key_bytes(private_key.public_key()).hex()}")


def generate_private_key() -> Ed25519PrivateKey:
    return Ed25519PrivateKey.generate()


def generate_auth_key(private_key: Ed25519PrivateKey = None) -> AuthKey:
    if not private_key:
        private_key = generate_private_key()

    auth_key = AuthKey.from_public_key(private_key.public_key())
    account_address = utils.account_address_hex(auth_key.account_address())
    print(f"~ Generated address: ${account_address}")

    return auth_key


if __name__ == "__main__":
    main()
