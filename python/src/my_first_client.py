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
from libra import AuthKey

from src.generate_keys_example import generate_private_key, generate_auth_key


def main():
    print("#1 Generate Keys")
    sender_private_key = generate_private_key()
    sender_auth_key: AuthKey = generate_auth_key(sender_private_key)
    print("#2 Create account")
    print("#3 Get account information")
    print("#4 Start event listener")
    print("#5 Get new events (1)")
    print("#6 Add money to account")
    print("#7 Get new events (2)")
    print("#8 Generate Keys")
    receiver_private_key = generate_private_key()
    receiver_auth_key: AuthKey = generate_auth_key(receiver_private_key)
    print("#9 Create second account")
    print("#10 Generate IntentIdentifier")
    print("#11 Deserialize IntentIdentifier")
    print("#12 Peer 2 peer transaction")
    print("#13 Get new events (3)")
    print("#14 Stop event listener")


if __name__ == "__main__":
    main()
