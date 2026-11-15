#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
mkdir -p out
javac -encoding UTF-8 -d out src/main/java/com/oasis/atm/*.java
java -cp out com.oasis.atm.Main
