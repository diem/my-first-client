import requests

from src.testnet import FAUCET_URL


def mint(authkey: str, amount: int, currency_code: str) -> int:
    session = requests.Session()
    response = session.post(
        FAUCET_URL,
        params={
            "amount": amount,
            "auth_key": authkey,
            "currency_code": currency_code,
        },
    )
    response.raise_for_status()
    return int(response.text)
