import requests

from src.generate_keys_example import generate_auth_key
from src.testnet import FAUCET_URL

"""
 mint_example demonstrates how to add currencies to account on the Libra blockchain
 The mint also use to create new account by adding currencies base on new auth_key
"""


def main():
    auth_key = generate_auth_key()

    # use mint to create new account
    mint(auth_key.hex(), 192000000, "LBR")


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


if __name__ == "__main__":
    main()
