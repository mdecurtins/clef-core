#!/bin/sh

echo "pwd is $(pwd)"

ls -l

echo "JARFILE is: $JARFILE"
echo "JAVA_OPTIONS is: $JAVA_OPTIONS"

echo "Java path is $(which java)"

echo "looking for jarfile"

if [ $(ls -la | grep -q $JARFILE) == 0 ]
then
	echo "found jarfile $JARFILE in $(pwd)"
else
	echo "did not find $JARFILE in $(pwd)!!"
fi

echo "running ls -l *.jar"
ls -l *.jar

echo "contents of clef/target:"
ls -l clef/target


echo "Attempting to run java jar:"
echo "cmd is: java $JAVA_OPTIONS -jar $JARFILE"

java $JAVA_OPTIONS -jar ./$JARFILE
