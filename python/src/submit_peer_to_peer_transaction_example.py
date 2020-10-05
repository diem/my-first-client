import time

from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from libra import jsonrpc, stdlib, utils, libra_types
from libra.libra_types import AccountAddress

from src.testnet import JSON_RPC_URL, CHAIN_ID


def submit_peer_to_peer_transaction(private_key: Ed25519PrivateKey,
                                    amount,
                                    receiver_account_address: AccountAddress,
                                    sender_account_address: AccountAddress,
                                    sequence_number,
                                    currency: str):
    # connect to testnet
    client = jsonrpc.Client(JSON_RPC_URL)
    # create script
    script = generate_script(amount, currency, receiver_account_address)
    # create transaction
    raw_transaction = generate_raw_transaction(currency, script, sender_account_address, sequence_number)
    # sign transaction
    signed_txn = sign_transaction(private_key, raw_transaction)
    # submit transaction
    client.submit(signed_txn)
    # wait for transaction
    client.wait_for_transaction(signed_txn)


def sign_transaction(private_key, raw_transaction):
    signature = private_key.sign(utils.raw_transaction_signing_msg(raw_transaction))
    public_key_bytes = utils.public_key_bytes(private_key.public_key())
    signed_txn = utils.create_signed_transaction(raw_transaction, public_key_bytes, signature)

    return signed_txn


def generate_raw_transaction(currency, script, sender_account_address, sequence_number):
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

    return raw_transaction


def generate_script(amount, currency, receiver_account_address):
    return stdlib.encode_peer_to_peer_with_metadata_script(
        currency=utils.currency_code(currency),
        payee=receiver_account_address,
        amount=amount,
        metadata=b'',  # no requirement for metadata and metadata signature
        metadata_signature=b'',
    )
