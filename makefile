.PHONY: ajava python typescript

help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

ajava: ## Build shaded jar and execute all code samples
ifeq ($(shell java -version 2>&1 | grep "1.8" >/dev/null; printf $$?),0)
	@echo "Found version"
else
	$(error "Could not find correct java version, please install 1.8")
endif

	cd java/; ./make-java.sh

python: ## Python Samples
	@echo "python"

typescript: ## Typescript Samples
	@echo "typescript"
