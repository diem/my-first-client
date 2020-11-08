.PHONY: java python typescript

help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

all: java python typescript

java: ## Build and execute all the Java code samples
# verify java version is 1.8
ifeq ($(shell java -version 2>&1 | grep "1.8" >/dev/null; printf $$?),0)
	@echo "Found correct java version"
else
	$(error "Could not find correct java version, please install 1.8")
endif

#verify gradle version is 5+
	@test "$(shell gradle -v | grep Gradle | grep -o '[0-9]\+.[0-9]\+.[0-9]\+' | cut --delimiter=. --fields=1)" -ge 5 || (echo "Could not find correct gradle version, please install 5+"; exit 13)
	@echo Found correct gradle version

	cd java/; ./make-java.sh

python: ## Install and execute all the Python examples
# verify python version
ifeq ($(shell python -V 2>&1 | grep "3.7.*" >/dev/null; printf $$?),0)
	@echo "Found correct python version"
else
	$(error "Could not find correct python version, please install 3.7.7")
endif

# verify pipenv existence
ifeq (, $(shell which pipenv))
	$(error "Please install pipenv")
endif

	cd python/; ./make-python.sh

typescript: ## Build and execute all the typescript code samples
# verify npm existence
ifeq (, $(shell which npm))
	$(error "No npm was found, please install npm to continue")
else
	@echo "found npm"
endif

	cd typescript/; ./make-typescript.sh
