"""
 This code demonstrates basic flow for working with the LibraClient.
 1. Generate keys
 2. Create account - by minting
 3. Get account information
 4. Start events listener
 5. Add money to existing account (mint)
 6. Generate keys for second account
 7. Create second account (for the benefit of the following transaction)
 8. Generate IntentIdentifier
 9. Deserialize IntentIdentifier
 10. Transfer money between accounts (peer to peer transaction)
"""
from libra import AuthKey

from generate_keys_example import generate_private_key, generate_auth_key
from get_account_info_example import get_account_info
from get_events_example import subscribe
from intent_identifier_example import generate_intent_identifier, decode_intent
from mint_example import mint
from submit_peer_to_peer_transaction_example import submit_peer_to_peer_transaction

CURRENCY = "Coin1"


def main():
    print("#1 Generate Keys")
    sender_private_key = generate_private_key()
    sender_auth_key: AuthKey = generate_auth_key(sender_private_key)

    print("#2 Create account")
    mint(sender_auth_key.hex(), 1340000000, CURRENCY)

    print("#3 Get account information")
    sender_account = get_account_info(sender_auth_key.account_address())

    events_key = sender_account.received_events_key
    print("#4 Start event listener")
    subscribe(events_key)

    print("#5 Add money to account")
    mint(sender_auth_key.hex(), 270000000, CURRENCY)

    print("#6 Generate Keys")
    receiver_private_key = generate_private_key()
    receiver_auth_key: AuthKey = generate_auth_key(receiver_private_key)

    print("#7 Create second account")
    mint(receiver_auth_key.hex(), 2560000000, CURRENCY)

    print("#8 Generate IntentIdentifier")
    encoded_intent_identifier = generate_intent_identifier(receiver_auth_key.account_address(), 130000000, CURRENCY)

    print("#9 Deserialize IntentIdentifier")
    intent_identifier = decode_intent(encoded_intent_identifier)

    print("#10 Peer 2 peer transaction")
    submit_peer_to_peer_transaction(sender_private_key,
                                    intent_identifier.amount,
                                    receiver_auth_key.account_address(),
                                    sender_auth_key.account_address(),
                                    sender_account.sequence_number,
                                    CURRENCY)


if __name__ == "__main__":
    main()
