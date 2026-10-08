#!/usr/bin/env sh
# Uso: ./run.sh        -> executa a demonstração
#      ./run.sh test   -> executa os testes
set -e
rm -rf out
SRC=$(find src/main/java -name '*.java')
if [ "$1" = "test" ]; then
  javac -encoding UTF-8 --release 17 -d out $SRC $(find src/test/java -name '*.java')
  java -Dstdout.encoding=UTF-8 -cp out br.com.cafeteria.Testes
else
  javac -encoding UTF-8 --release 17 -d out $SRC
  java -Dstdout.encoding=UTF-8 -cp out br.com.cafeteria.Main
fi
