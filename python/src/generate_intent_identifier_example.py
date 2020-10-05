from libra import identifier, utils
from libra.libra_types import AccountAddress


def generate_intent_identifier(address: AccountAddress, amount: int, currency: str) -> str:
    account_identifier = identifier.encode_account(utils.account_address_hex(address), None, identifier.TLB)
    intent_identifier = identifier.encode_intent(account_identifier, currency, amount)
    print(f"intent_identifier: {intent_identifier}")

    return intent_identifier
