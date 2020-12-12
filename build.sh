#!/usr/bin/env bash
mvn clean install -DskipTests
java -jar target/dreambox-0.0.1-SNAPSHOT.jar
