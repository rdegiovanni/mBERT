ARGS="$@"
mvn exec:java -Dexec.mainClass="Main" -Dexec.args="$ARGS"