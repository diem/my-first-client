import libra
from libra import identifier, utils
from libra.identifier import decode_intent
from libra.libra_types import AccountAddress

from generate_keys_example import generate_auth_key

""" intent_identifier_example demonstrates the IntentIdentifier generation and deserialization """


def main():
    # create intent identifier
    auth_key = generate_auth_key()
    intent_identifier = generate_intent_identifier(auth_key.account_address(), 130000000, "Coin1")

    # deserialize IntentIdentifier
    intent_identifier = decode_intent(intent_identifier)

    print(f"~ Account (HEX) from intent: ${utils.account_address_hex(intent_identifier.account_address)}")
    print(f"~ Amount from intent: ${intent_identifier.amount}")
    print(f"~ Currency from intent: ${intent_identifier.currency_code}")


def generate_intent_identifier(address: AccountAddress, amount: int, currency: str) -> str:
    account_identifier = identifier.encode_account(utils.account_address_hex(address), None, identifier.TLB)
    intent_identifier = identifier.encode_intent(account_identifier, currency, amount)
    print(f"~ Encoded IntentIdentifier: {intent_identifier}")

    return intent_identifier


def decode_intent(encoded_intent_identifier):
    return libra.identifier.decode_intent(encoded_intent_identifier, identifier.TLB)


if __name__ == "__main__":
    main()
