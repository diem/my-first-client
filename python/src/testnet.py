from libra import libra_types, serde_types

FAUCET_URL = "https://testnet.libra.org/mint"
JSON_RPC_URL = "https://testnet.libra.org/v1"
CHAIN_ID = libra_types.ChainId(value=serde_types.uint8(2))
