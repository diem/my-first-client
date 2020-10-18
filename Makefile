.PHONY: java python typescript

GRADLE_VERSION:=$(shell gradle -v | grep Gradle)

help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

java: ## Build and execute all the Java code samples
ifeq ($(shell java -version 2>&1 | grep "1.8" >/dev/null; printf $$?),0)
	@echo "Found correct java version"
else
	$(error "Could not find correct java version, please install 1.8")
endif

#	echo "$$(echo $$PATH)"
#	test "$$(gradle -v | grep Gradle | grep -o '[0-9]\+.[0-9]\+.[0-9]\+' | cut --delimiter=. --fields=1)" -ge 9 || exit 4

	@test "$$(echo '$(GRADLE_VERSION)' | grep Gradle | grep -o '[0-9]\+.[0-9]\+.[0-9]\+' | cut --delimiter=. --fields=1)" -ge 5 || (echo "Could not find correct gradle version, please install 5+"; exit 13)
	@echo Found correct gradle version

	cd java/; ./make-java.sh

python: ## Python Samples
	@echo "python"

typescript: ## Typescript Samples
	@echo "typescript"
