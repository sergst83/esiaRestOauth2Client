@echo off

set JAVA_HOME="C:\Program Files\Java\jre1.8.0_112"

set APP_JAR_PATH="C:\esiaRestOauth2Client\esiaRestOauth2Client-1.0-SNAPSHOT-all.jar"
set APP_CFG="C:\esiaRestOauth2Client\resources"
set LOG4J_CONF=%APP_CFG%\log4j.xml

"%JAVA_HOME%\bin\java.exe" "-Dspring.profiles.active=file" "-DAPP_CFG=%APP_CFG%" "-Dlog4j.configuration=file:%LOG4J_CONF%" -jar "%APP_JAR_PATH%"