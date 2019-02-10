SET _JAR_PATH=%1
IF "%_JAR_PATH%"=="" SET _JAR_PATH=target/resource-management-0.0.1-SNAPSHOT.jar

SET _CONFIG_PATH=%2
IF "%_CONFIG_PATH%"=="" SET _CONFIG_PATH=D:\\logs\\application.properties

java -Dspring.config.location=%_CONFIG_PATH% -jar %_JAR_PATH%