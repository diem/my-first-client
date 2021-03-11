#!/usr/bin/env bash

RED='\033[0;31m'
PURPLE='\033[0;35m'
GREEN='\033[0;32m'
NC='\033[0m'
NL="\n"

gradle shadowJar || exit

printf "${NL}${GREEN}Build Done!${NC} jar could be found at ${PWD}/build/libs${NL}${NL}"

cd build/libs/

printf "${GREEN}Executing Java examples${NC}${NL}"

printf "${PURPLE}Generate keys example:${NC}${NL}"
printf "execute: java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GenerateKeysExample"
java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GenerateKeysExample

printf "${PURPLE}Mint Example:${NC}${NL}"
printf "execute: java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.MintExample"
java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.MintExample

printf "${PURPLE}Get Account Example:${NC}${NL}"
printf "execute: java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GetAccountInfoExample"
java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GetAccountInfoExample

printf "${PURPLE}Intent Identifier Example:${NC}${NL}"
printf "execute: java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.IntentIdentifierExample"
java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.IntentIdentifierExample

printf "${PURPLE}Submit Peer To Peer Transaction Example:${NC}${NL}"
printf "execute: java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.SubmitPeerToPeerTransactionExample"
java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.SubmitPeerToPeerTransactionExample

printf "${PURPLE}Get Events Example:${NC}${NL}"
printf "execute: java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GetEventsExample"
java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GetEventsExample


