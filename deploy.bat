SET _JAR_PATH=%1
IF "%_JAR_PATH%"=="" SET _JAR_PATH=target/resource-management-0.0.1-SNAPSHOT.jar

SET _CONFIG_PATH=%2
IF "%_CONFIG_PATH%"=="" SET _CONFIG_PATH=src/main/resources/application.properties

SET _LOG_CONFIG_PATH=%3
IF "%_LOG_CONFIG_PATH%"=="" SET _LOG_CONFIG_PATH=src/main/resources-filtered/log4j2.properties

java -Dlogging.config=%_LOG_CONFIG_PATH% -Dspring.config.location=%_CONFIG_PATH% -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager -jar %_JAR_PATH%