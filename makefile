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
	@echo "python"

typescript:
	@echo "typescript"
