#!/usr/bin/env bash

RED='\033[0;31m'
PURPLE='\033[0;35m'
GREEN='\033[0;32m'
NC='\033[0m'
NL="\n"

gradle shadowJar

printf "${NL}${GREEN}Build Done!${NC} jar could be found at <current-dir>\java\build\lib${NL}"

cd build/libs/ || exit
printf "moved to: ${PWD}${NL}"

printf "${RED}Execute java examples:${NC}${NL}"

printf "${PURPLE}Generate keys example:${NC}${NL}"
printf "execute: java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GenerateKeysExample"
java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GenerateKeysExample

printf "${PURPLE}Get Account Example:${NC}${NL}"
printf "execute: java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GetAccountInfoExample"
java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GetAccountInfoExample

printf "${PURPLE}Get Events Example:${NC}${NL}"
printf "execute: java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GetEventsExample"
java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GetEventsExample

printf "${PURPLE}Intent Identifier Example:${NC}${NL}"
printf "execute: java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.IntentIdentifierExample"
java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.IntentIdentifierExample

printf "${PURPLE}Mint Example:${NC}${NL}"
printf "execute: java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.MintExample"
java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.MintExample

printf "${PURPLE}Submit Peer To Peer Transaction Example:${NC}${NL}"
printf "execute: java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.SubmitPeerToPeerTransactionExample"
java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.SubmitPeerToPeerTransactionExample
