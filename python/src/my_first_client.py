"""
 This code demonstrates basic flow for working with the LibraClient.
 1. Generate keys
 2. Create account - by minting
 3. Get account information
 4. Start events listener
 5. Get new events (1)
 6. Add money to existing account (mint)
 7. Get new events (2)
 8. Generate keys for second account
 9. Create second account (for the benefit of the following transaction)
 10. Generate IntentIdentifier
 11. Deserialize IntentIdentifier
 12. Transfer money between accounts (peer to peer transaction)
 13. Get new events (3)
 14. Close listener
"""
from libra import AuthKey, identifier
from libra.identifier import decode_intent

from src.generate_intent_identifier_example import generate_intent_identifier
from src.generate_keys_example import generate_private_key, generate_auth_key, extract_account_address
from src.get_account_info_example import get_account_info
from src.get_events_example import GetEventsExample
from src.mint_example import mint
from src.submit_peer_to_peer_transaction_example import submit_peer_to_peer_transaction


def main():
    print("#1 Generate Keys")
    sender_private_key = generate_private_key()
    sender_auth_key: AuthKey = generate_auth_key(sender_private_key)
    print("#2 Create account")
    mint(sender_auth_key.hex(), 1340000000, "LBR")

    sender_account_address = extract_account_address(sender_auth_key)

    print("#3 Get account information")
    sender_account = get_account_info(sender_account_address)

    events_key = sender_account.received_events_key
    print("#4 Start event listener")
    get_events_example = GetEventsExample(events_key)

    print("#5 Get new events (1)")
    new_events = get_events_example.get()
    print(f"{len(new_events)} new events was found")

    print("#6 Add money to account")
    mint(sender_auth_key.hex(), 270000000, "LBR")

    print("#7 Get new events (2)")
    new_events = get_events_example.get()
    print(f"{len(new_events)} new events was found")

    print("#8 Generate Keys")
    receiver_private_key = generate_private_key()
    receiver_auth_key: AuthKey = generate_auth_key(receiver_private_key)
    print("#9 Create second account")
    mint(receiver_auth_key.hex(), 2560000000, "LBR")
    print("#10 Generate IntentIdentifier")
    encoded_intent_identifier = generate_intent_identifier(receiver_auth_key.account_address(), 130000000, "LBR")
    print("#11 Deserialize IntentIdentifier")
    intent_identifier = decode_intent(encoded_intent_identifier, identifier.TLB)
    print("#12 Peer 2 peer transaction")
    submit_peer_to_peer_transaction(sender_private_key,
                                    intent_identifier.amount,
                                    receiver_auth_key.account_address(),
                                    sender_auth_key.account_address(),
                                    sender_account.sequence_number,
                                    "LBR")

    print("#13 Get new events (3)")
    new_events = get_events_example.get()
    print(f"{len(new_events)} new events was found")

    print("#14 Stop event listener")
    get_events_example.stop()


if __name__ == "__main__":
    main()
