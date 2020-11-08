#!/usr/bin/env bash

RED='\033[0;31m'
PURPLE='\033[0;35m'
GREEN='\033[0;32m'
NC='\033[0m'
NL="\n"

npm install || exit
printf "${NL}${GREEN}Installation Done!${NC}${NL}"
#printf "Hello, %s\n" "$NAME"
printf "${RED}Execute typescript examples:${NC}${NL}"

printf "${PURPLE}Generate keys example:${NC}${NL}"
printf "execute: npx ts-node src/generateKeysExample.ts"
npx ts-node src/generateKeysExample.ts

printf "${PURPLE}Mint Example:${NC}${NL}"
printf "execute: mintExample.ts"
npx ts-node src/mintExample.ts

printf "${PURPLE}Get Account Example:${NC}${NL}"
printf "execute: npx ts-node src/getAccountExample.ts"
npx ts-node src/getAccountExample.ts

printf "${PURPLE}Intent Identifier Example:${NC}${NL}"
printf "execute: npx ts-node src/intentIdentifierExample.ts"
npx ts-node src/intentIdentifierExample.ts

printf "${PURPLE}Submit Peer To Peer Transaction Example:${NC}${NL}"
printf "execute: npx ts-node src/submitPeerToPeerTransactionExample.ts"
npx ts-node src/submitPeerToPeerTransactionExample.ts

printf "${PURPLE}Get Events Example:${NC}${NL}"
printf "execute: npx ts-node src/getEventsExample.ts"
npx ts-node src/getEventsExample.ts
