#!/usr/bin/env sh

find -name "*.java" > sources.txt
javac -d bin @sources.txt