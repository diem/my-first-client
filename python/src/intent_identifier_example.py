""" intent_identifier_example demonstrates the IntentIdentifier generation and deserialization """

from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from diem import identifier, utils, AuthKey


def main():
    # generate private key
    private_key = Ed25519PrivateKey.generate()
    # generate auth key
    auth_key = AuthKey.from_public_key(private_key.public_key())

    # create intent identifier
    account_identifier = identifier.encode_account(
        utils.account_address_hex(auth_key.account_address()), None, identifier.TDM
    )
    encoded_intent_identifier = identifier.encode_intent(account_identifier, "XUS", 10000000)
    print(f"Encoded IntentIdentifier: {encoded_intent_identifier}")

    # deserialize IntentIdentifier
    intent_identifier = identifier.decode_intent(encoded_intent_identifier, identifier.TDM)

    print(f"Account (HEX) from intent: {utils.account_address_hex(intent_identifier.account_address)}")
    print(f"Amount from intent: {intent_identifier.amount}")
    print(f"Currency from intent: {intent_identifier.currency_code}")


if __name__ == "__main__":
    main()
