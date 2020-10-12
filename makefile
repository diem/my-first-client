all: ajava python typescript
	@echo "All"

ajava:
	cd java/; pwd; gradle shadowJar; \
	cd build/libs/; pwd; \
	java -cp java-code-examples-1.0-SNAPSHOT-all.jar example.GetAccountInfoExample

python:
	@echo "python"

typescript:
	@echo "typescript"
