import time

from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from libra import jsonrpc, stdlib, utils, libra_types
from libra.libra_types import AccountAddress
from libra.testnet import JSON_RPC_URL, CHAIN_ID

from generate_keys_example import generate_private_key, generate_auth_key
from get_account_info_example import get_account_info
from mint_example import mint

CURRENCY = "Coin1"

"""
submit_peer_to_peer_transaction_example demonstrates currencies transfer between 2 accounts on the Libra blockchain
"""


def main():
    # create sender account
    sender_private_key = generate_private_key()
    sender_auth_key = generate_auth_key(sender_private_key)
    mint(sender_auth_key.hex(), 1340000000, CURRENCY)

    # get sender account
    sender_account = get_account_info(sender_auth_key.account_address())

    # create receiver account
    receiver_auth_key = generate_auth_key()
    mint(receiver_auth_key.hex(), 10000000, CURRENCY)

    submit_peer_to_peer_transaction(sender_private_key,
                                    130000000,
                                    receiver_auth_key.account_address(),
                                    sender_auth_key.account_address(),
                                    sender_account.sequence_number,
                                    CURRENCY)


def submit_peer_to_peer_transaction(private_key: Ed25519PrivateKey,
                                    amount,
                                    receiver_account_address: AccountAddress,
                                    sender_account_address: AccountAddress,
                                    sequence_number,
                                    currency: str):
    # connect to testnet
    client = jsonrpc.Client(JSON_RPC_URL)
    # create script
    script = stdlib.encode_peer_to_peer_with_metadata_script(
        currency=utils.currency_code(currency),
        payee=receiver_account_address,
        amount=amount,
        metadata=b'',  # no requirement for metadata and metadata signature
        metadata_signature=b'',
    )
    # create transaction
    raw_transaction = libra_types.RawTransaction(
        sender=sender_account_address,
        sequence_number=sequence_number,
        payload=libra_types.TransactionPayload__Script(script),
        max_gas_amount=1_000_000,
        gas_unit_price=0,
        gas_currency_code=currency,
        expiration_timestamp_secs=int(time.time()) + 30,
        chain_id=CHAIN_ID,
    )
    # sign transaction
    signed_txn = sign_transaction(private_key, raw_transaction)
    # submit transaction
    client.submit(signed_txn)
    # wait for transaction
    client.wait_for_transaction(signed_txn)


def sign_transaction(private_key, raw_transaction):
    signature = private_key.sign(utils.raw_transaction_signing_msg(raw_transaction))
    public_key_bytes = utils.public_key_bytes(private_key.public_key())

    return utils.create_signed_transaction(raw_transaction, public_key_bytes, signature)


if __name__ == "__main__":
    main()
