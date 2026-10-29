
set -e
rm -rf out && mkdir out
javac -encoding UTF-8 -d out $(find src/main -name "*.java")
java -Dfile.encoding=UTF-8 -cp out com.cipa.nomina.Main
