.PHONY: ajava python typescript

help:
	@echo "HELP!!!!"

ajava:
ifeq ($(shell java -version 2>&1 | grep "1.8" >/dev/null; printf $$?),0)
	@echo "Found version"
else
	$(error "Could not find correct java version, please install 1.8")
endif

	cd java/; ./make-java.sh

python:
ifeq ($(shell python -V 2>&1 | grep "3.7.7" >/dev/null; printf $$?),0)
	echo "Found version"
else
	$(error "Could not find correct python version, please install 3.7.7")
endif

ifeq (, $(shell which pipenv))
	$(error "Please install pipenv")
endif

	cd python/; ./make-python.sh
typescript:
	@echo "typescript"
