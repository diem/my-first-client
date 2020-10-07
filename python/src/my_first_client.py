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

from src.generate_keys_example import generate_private_key, generate_auth_key, extract_account_address
from src.get_account_info_example import get_account_info
from src.intent_identifier_example import generate_intent_identifier, decode_intent
from src.mint_example import mint


def main():
    print("#1 Generate Keys")
    sender_private_key = generate_private_key()
    sender_auth_key: AuthKey = generate_auth_key(sender_private_key)
    print("#2 Create account")
    mint(sender_auth_key.hex(), 1340000000, "LBR")

    sender_account_address = extract_account_address(sender_auth_key)

    print("#3 Get account information")
    sender_account = get_account_info(sender_account_address)
    print("#4 Start event listener")
    print("#5 Add money to account")
    mint(sender_auth_key.hex(), 270000000, "LBR")

    print("#6 Generate Keys")
    receiver_private_key = generate_private_key()
    receiver_auth_key: AuthKey = generate_auth_key(receiver_private_key)
    print("#7 Create second account")
    mint(receiver_auth_key.hex(), 2560000000, "LBR")

    print("#8 Generate IntentIdentifier")
    encoded_intent_identifier = generate_intent_identifier(receiver_auth_key.account_address(), 130000000, "LBR")

    print("#9 Deserialize IntentIdentifier")
    intent_identifier = decode_intent(encoded_intent_identifier)

    print("#10 Peer 2 peer transaction")


if __name__ == "__main__":
    main()
