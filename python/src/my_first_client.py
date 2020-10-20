"""
 This code demonstrates basic flow for working with the LibraClient.
 1. connect to testnet
 2. Generate keys
 3. Create account - by minting
 4. Get account information
 5. Start events listener
 6. Add money to existing account (mint)
 7. Generate keys for second account
 8. Create second account (for the benefit of the following transaction)
 9. Generate IntentIdentifier
 10. Deserialize IntentIdentifier
 11. Transfer money between accounts (peer to peer transaction)
"""
from libra import AuthKey, testnet

import generate_keys_example
import get_account_example
import get_events_example
import intent_identifier_example
import mint_example
import submit_peer_to_peer_transaction_example
from mint_example import mint

CURRENCY = "Coin1"


def main():
    print("#1 Generate Keys")
    client = testnet.create_client();

    print("#2 Generate Keys")
    sender_private_key = generate_keys_example.generate_private_key()
    sender_auth_key: AuthKey = generate_keys_example.generate_auth_key(sender_private_key)

    print("#3 Create account")
    mint_example.mint(client, sender_auth_key.hex(), 1340000000, CURRENCY)

    print("#4 Get account information")
    sender_account = get_account_example.get_account(client, sender_auth_key.account_address())

    events_key = sender_account.received_events_key
    print("#5 Start event listener")
    get_events_example.subscribe(client, events_key)

    print("#6 Add money to account")
    mint_example.mint(client, sender_auth_key.hex(), 270000000, CURRENCY)

    print("#7 Generate Keys")
    receiver_auth_key: AuthKey = generate_keys_example.generate_auth_key()

    print("#8 Create second account")
    mint(client, receiver_auth_key.hex(), 2560000000, CURRENCY)

    print("#9 Generate IntentIdentifier")
    encoded_intent_identifier = intent_identifier_example.generate_intent_identifier(
        receiver_auth_key.account_address(), 130000000, CURRENCY)

    print("#10 Deserialize IntentIdentifier")
    intent_identifier = intent_identifier_example.decode_intent(encoded_intent_identifier)

    print("#11 Peer 2 peer transaction")
    submit_peer_to_peer_transaction_example.submit_peer_to_peer_transaction(client,
                                                                            sender_private_key,
                                                                            intent_identifier.amount,
                                                                            receiver_auth_key.account_address(),
                                                                            sender_auth_key.account_address(),
                                                                            sender_account.sequence_number,
                                                                            CURRENCY)


if __name__ == "__main__":
    main()
