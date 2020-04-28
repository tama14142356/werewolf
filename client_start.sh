#!/bin/bash
javac Client_Main.java -encoding UTF-8
javac Server_Main.java -encoding UTF-8
for i in {1..6}
do
	java Client_Main cui-$i &
done
java Server_Main
