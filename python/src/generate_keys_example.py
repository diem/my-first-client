from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from libra import AuthKey


def generate_private_key() -> Ed25519PrivateKey:
    return Ed25519PrivateKey.generate()


def generate_auth_key(private_key: Ed25519PrivateKey) -> AuthKey:
    return AuthKey.from_public_key(private_key.public_key())


def extract_account_address(auth_key: AuthKey):
    account_address = auth_key.account_address();
    print(account_address)
    # Hex.encode(account_address.value);
