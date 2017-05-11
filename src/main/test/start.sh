#!/usr/bin/env bash

JAVA_HOME=/home/sergst/JDKs/jdk8

#APP_JAR_PATH=/home/sergst/workspace/tests/esiaRestOauth2Client/build/libs/esiaRestOauth2Client-1.0-SNAPSHOT.jar
APP_JAR_PATH=/home/sergst/workspace/tests/esiaRestOauth2Client/build/libs/esiaRestOauth2Client-1.0-SNAPSHOT-all.jar

APP_CFG=/home/sergst/workspace/tests/esiaRestOauth2Client/src/main/test/resources

#LIB_PATH=/home/sergst/workspace/tests/esiaRestOauth2Client/build/libs/lib

#CLASSPATH=$APP_JAR_PATH:$(echo $LIB_PATH/*.jar | tr ' ' ':')
LOG4J_CONF=/home/sergst/workspace/tests/esiaRestOauth2Client/src/main/test/resources/log4j.xml

#echo "$JAVA_HOME/bin/java -cp $CLASSPATH -Dspring.profiles.active=file -DAPP_CFG=$APP_CFG ru.rtlabs.Application"
#$JAVA_HOME/bin/java -cp $CLASSPATH -Dspring.profiles.active=file -DAPP_CFG=$APP_CFG ru.rtlabs.Application
$JAVA_HOME/bin/java -Dspring.profiles.active=file -DAPP_CFG=$APP_CFG -Dlog4j.configuration=file:$LOG4J_CONF -jar $APP_JAR_PATH