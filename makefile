RED := '\033[0;31m'
PURPLE := '\033[0;35m'
GREEN := '\033[0;32m'
NC := '\033[0m'

all: ajava python typescript
	@echo "All"

ajava:
	cd java/; \
	pwd; \
	gradle shadowJar; \
	echo ""; \
	echo "${GREEN}Build Done!${NC} jar could be found at <current-dir>java\build\lib"; \
	echo ""; \
	cd build/libs/; \
	pwd; \
	echo ""; \
	echo "${RED}Execute java examples:${NC}"; \
	echo ""; \
	echo "${PURPLE}Generate keys example:${NC}"; \
	java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GenerateKeysExample; \
	echo ""; \
	echo "${PURPLE}Get Account Example:${NC}"; \
	java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GetAccountInfoExample; \
	echo ""; \
	echo "${PURPLE}Get Events Example:${NC}"; \
        java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GetEventsExample; \
        echo ""; \
	echo "${PURPLE}Intent Identifier Example:${NC}"; \
        java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.IntentIdentifierExample; \
        echo ""; \
	echo "${PURPLE}Mint Example:${NC}"; \
        java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.MintExample; \
        echo ""; \
	echo "${PURPLE}Submit Peer To Peer Transaction Example:${NC}"; \
        java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.SubmitPeerToPeerTransactionExample; \
        echo ""; \


python:
	@echo "python"

typescript:
	@echo "typescript"
