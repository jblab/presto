#!/bin/bash

cd presto-spi && mvn -DskipTests clean install
cd ../presto-plugin-toolkit  && mvn -DskipTests clean install
cd ../presto-client && mvn -DskipTests clean install
cd ../presto-parser && mvn -DskipTests clean install
cd ../presto-array && mvn -DskipTests clean install
cd ../presto-matching && mvn -DskipTests clean install
cd ../presto-memory-context && mvn -DskipTests clean install
cd ../presto-tpch && mvn -DskipTests clean install
cd ../presto-main && mvn -DskipTests clean install
cd ../presto-resource-group-managers && mvn -DskipTests clean install
cd ../presto-atop && mvn -DskipTests clean install
cd ../presto-jmx && mvn -DskipTests clean install
cd ../presto-record-decoder  && mvn -DskipTests clean install
cd ../presto-redis  && mvn -DskipTests clean install
cd ../presto-memory  && mvn -DskipTests clean install
cd ../presto-orc  && mvn -DskipTests clean install
cd ../presto-parquet  && mvn -DskipTests clean install
cd ../presto-rcfile  && mvn -DskipTests clean install
cd ../presto-hive  && mvn -DskipTests clean install
cd ../presto-hive-hadoop2   && mvn -DskipTests clean install
cd ../presto-example-http   && mvn -DskipTests clean install
cd ../presto-local-file  && mvn -DskipTests clean install
cd ../presto-base-jdbc  && mvn -DskipTests clean install
cd ../presto-mysql  && mvn -DskipTests clean install
cd ../presto-jdbc  && mvn -DskipTests clean install
cd ../presto-cli  && mvn -DskipTests clean install
cd ../presto-thrift-connector-api  && mvn -DskipTests clean install
cd ../presto-thrift-connector  && mvn -DskipTests clean install
cd ../presto-session-property-managers  && mvn -DskipTests clean install
cd ../presto-server  && mvn -DskipTests clean install