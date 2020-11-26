"""
 This code demonstrates basic flow for working with the DiemClient.
 1. Connect to testnet
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
import time

import libra
from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from libra import AuthKey, testnet, identifier, utils, libra_types, stdlib
from libra.testnet import CHAIN_ID

import get_events_example

CURRENCY = "Coin1"


def main():
    print("#1 connect to testnet")
    client = testnet.create_client();

    print("#2 Generate Keys")
    # generate private key for sender account
    sender_private_key = Ed25519PrivateKey.generate()
    # generate auth key for sender account
    sender_auth_key = AuthKey.from_public_key(sender_private_key.public_key())
    print(f"Generated sender address: {utils.account_address_hex(sender_auth_key.account_address())}")

    print("#3 Create account")
    faucet = testnet.Faucet(client)
    testnet.Faucet.mint(faucet, sender_auth_key.hex(), 100000000, CURRENCY)

    print("#4 Get account information")
    sender_account = client.get_account(sender_auth_key.account_address())

    events_key = sender_account.received_events_key
    print("#5 Start event listener")
    get_events_example.subscribe(client, events_key)

    print("#6 Add money to account")
    faucet = testnet.Faucet(client)
    testnet.Faucet.mint(faucet, sender_auth_key.hex(), 10000000, CURRENCY)

    print("#7 Generate Keys")
    # generate private key for receiver account
    receiver_private_key = Ed25519PrivateKey.generate()
    # generate auth key for receiver account
    receiver_auth_key = AuthKey.from_public_key(receiver_private_key.public_key())
    print(f"Generated receiver address: {utils.account_address_hex(receiver_auth_key.account_address())}")

    print("#8 Create second account")
    faucet = testnet.Faucet(client)
    testnet.Faucet.mint(faucet, receiver_auth_key.hex(), 1000000, CURRENCY)

    print("#9 Generate IntentIdentifier")
    account_identifier = identifier.encode_account(utils.account_address_hex(receiver_auth_key.account_address()), None,
                                                   identifier.TLB)
    encoded_intent_identifier = identifier.encode_intent(account_identifier, CURRENCY, 10000000)
    print(f"Encoded IntentIdentifier: {encoded_intent_identifier}")

    print("#10 Deserialize IntentIdentifier")
    intent_identifier = libra.identifier.decode_intent(encoded_intent_identifier, identifier.TLB)
    print(f"Account (HEX) from intent: {utils.account_address_hex(intent_identifier.account_address)}")
    print(f"Amount from intent: {intent_identifier.amount}")
    print(f"Currency from intent: {intent_identifier.currency_code}")

    print("#11 Peer 2 peer transaction")
    # create script
    script = stdlib.encode_peer_to_peer_with_metadata_script(
        currency=utils.currency_code(intent_identifier.currency_code),
        payee=intent_identifier.account_address,
        amount=intent_identifier.amount,
        metadata=b'',  # no requirement for metadata and metadata signature
        metadata_signature=b'',
    )
    # create transaction
    raw_transaction = libra_types.RawTransaction(
        sender=sender_auth_key.account_address(),
        sequence_number=sender_account.sequence_number,
        payload=libra_types.TransactionPayload__Script(script),
        max_gas_amount=1_000_000,
        gas_unit_price=0,
        gas_currency_code=CURRENCY,
        expiration_timestamp_secs=int(time.time()) + 30,
        chain_id=CHAIN_ID,
    )
    # sign transaction
    signature = sender_private_key.sign(utils.raw_transaction_signing_msg(raw_transaction))
    public_key_bytes = utils.public_key_bytes(sender_private_key.public_key())

    signed_txn = utils.create_signed_transaction(raw_transaction, public_key_bytes, signature)
    # submit transaction
    client.submit(signed_txn)
    # wait for transaction
    client.wait_for_transaction(signed_txn)


if __name__ == "__main__":
    main()
