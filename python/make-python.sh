#!/usr/bin/env bash

RED='\033[0;31m'
PURPLE='\033[0;35m'
GREEN='\033[0;32m'
NC='\033[0m'
NL="\n"

pipenv install
printf "${NL}${GREEN}Installation Done!${NC}${NL}"

printf "${RED}Execute java examples:${NC}${NL}"

printf "${PURPLE}Generate keys example:${NC}${NL}"
printf "execute: pipenv run python src/generate_keys_example.py${NL}"
pipenv run python src/generate_keys_example.py

printf "${PURPLE}Mint Example:${NC}${NL}"
printf "execute: pipenv run python src/mint_example.py${NL}"
printf "pwd: ${PWD}${NL}"
pipenv run python src/mint_example.py

printf "${PURPLE}Get Account Example:${NC}${NL}"
printf "execute: pipenv run python src/get_account_info_example.py${NL}"
pipenv run python src/get_account_info_example.py

printf "${PURPLE}Intent Identifier Example:${NC}${NL}"
printf "execute: pipenv run python src/intent_identifier_example.py${NL}"
pipenv run python src/intent_identifier_example.py

printf "${PURPLE}Submit Peer To Peer Transaction Example:${NC}${NL}"
printf "execute: pipenv run python src/submit_peer_to_peer_transaction_example.py${NL}"
pipenv run python src/submit_peer_to_peer_transaction_example.py

printf "${PURPLE}Get Events Example:${NC}${NL}"
printf "execute: pipenv run python src/get_events_example.py${NL}"
pipenv run python src/get_events_example.py
