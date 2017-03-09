CLJ_MODE ?= clj
SOURCES_BASE = src/mal/reader.clj src/mal/util.clj src/mal/printer.clj src/mal/eval.clj
SOURCES_LISP = src/mal/stepA_mal.clj
SRCS = $(SOURCES_BASE)
SOURCES = $(SOURCES_BASE) $(SOURCES_LISP)

all: $(if $(filter cljs,$(CLJ_MODE)),node_modules,deps)

dist: mal.jar mal

deps:
	lein deps

mal.jar: $(SOURCES)
	lein with-profile stepA uberjar
	cp target/mal-0.0.1-SNAPSHOT-standalone.jar $@

SHELL := bash
mal: mal.jar
	cat <(echo -e '#!/bin/sh\nexec java -jar "$$0" "$$@"') mal.jar > $@
	chmod +x mal

target/%.jar: src/mal/%.clj $(SRCS)
	lein with-profile $(word 1,$(subst _, ,$*)) uberjar

node_modules:
	npm install

clean:
	rm -rf target/ mal.jar mal

.PHONY: stats

stats: $(SOURCES)
	@wc $^
	@printf "%5s %5s %5s %s\n" `grep -E "^[[:space:]]*;|^[[:space:]]*$$" $^ | wc` "[comments/blanks]"
stats-lisp: $(SOURCES_LISP)
	@wc $^
	@printf "%5s %5s %5s %s\n" `grep -E "^[[:space:]]*;|^[[:space:]]*$$" $^ | wc` "[comments/blanks]"
