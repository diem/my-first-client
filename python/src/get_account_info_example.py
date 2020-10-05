from libra import jsonrpc
from libra.jsonrpc import Account

from src.testnet import JSON_RPC_URL


def get_account_info(account_address: str) -> Account:
    client = jsonrpc.Client(JSON_RPC_URL)
    return client.get_account(account_address)
