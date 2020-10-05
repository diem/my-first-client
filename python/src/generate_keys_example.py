from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from libra import AuthKey, utils


def generate_private_key() -> Ed25519PrivateKey:
    return Ed25519PrivateKey.generate()


def generate_auth_key(private_key: Ed25519PrivateKey) -> AuthKey:
    auth_key = AuthKey.from_public_key(private_key.public_key())
    account_address = extract_account_address(auth_key)
    print("~ Generated address: " + account_address)

    return auth_key


def extract_account_address(auth_key: AuthKey) -> str:
    return utils.account_address_hex(auth_key.account_address())
