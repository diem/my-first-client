from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from libra import AuthKey, utils

"""generate_keys_example demonstrates the required steps to generate keys for an account on the Libra blockchain"""


def main():
    private_key = generate_private_key()

    print(utils.public_key_bytes(private_key.public_key()).hex())

    auth_key = generate_auth_key(private_key)

    print(auth_key.hex())


def generate_private_key() -> Ed25519PrivateKey:
    return Ed25519PrivateKey.generate()


def generate_auth_key(private_key: Ed25519PrivateKey = None) -> AuthKey:
    if not private_key:
        private_key = generate_private_key()

    auth_key = AuthKey.from_public_key(private_key.public_key())
    account_address = extract_account_address(auth_key)
    print("~ Generated address: " + account_address)

    return auth_key


def extract_account_address(auth_key: AuthKey):
    account_address = auth_key.account_address();
    print(account_address)


if __name__ == "__main__":
    main()
