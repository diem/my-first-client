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

from src.generate_keys_example import generate_private_key, generate_auth_key


def main():
    print("#1 Generate Keys")
    sender_private_key = generate_private_key()
    sender_auth_key: AuthKey = generate_auth_key(sender_private_key)
    print("#2 Create account")
    print("#3 Get account information")
    print("#4 Start event listener")
    print("#5 Add money to account")
    print("#6 Generate Keys")
    receiver_private_key = generate_private_key()
    receiver_auth_key: AuthKey = generate_auth_key(receiver_private_key)
    print("#7 Create second account")
    print("#8 Generate IntentIdentifier")
    print("#9 Deserialize IntentIdentifier")
    print("#10 Peer 2 peer transaction")


if __name__ == "__main__":
    main()
