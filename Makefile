.PHONY: java python typescript

help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

java: ## Build and execute all the Java code examples
ifeq ($(shell java -version 2>&1 | grep "1.8" >/dev/null; printf $$?),0)
	@echo "Found version"
else
	$(error "Could not find correct java version, please install 1.8")
endif

	cd java/; ./make-java.sh

python: ## Install and execute all the Python examples
# verify python version
ifeq ($(shell python -V 2>&1 | grep "3.7.7" >/dev/null; printf $$?),0)
	echo "Found correct python version"
else
	$(error "Could not find correct python version, please install 3.7.7")
endif

# verify pipenv existence
ifeq (, $(shell which pipenv))
	$(error "Please install pipenv")
endif

	cd python/; ./make-python.sh
typescript:
	@echo "typescript"
