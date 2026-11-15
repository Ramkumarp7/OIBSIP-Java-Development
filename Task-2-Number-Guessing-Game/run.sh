#!/usr/bin/env sh
cd "$(dirname "$0")"
mkdir -p out
javac -encoding UTF-8 -d out src/main/java/com/oasis/guessing/*.java && java -cp out com.oasis.guessing.Main
