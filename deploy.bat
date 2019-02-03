SET _INSTALL_PATH=%1
IF "%_INSTALL_PATH%"=="" SET _INSTALL_PATH=target

SET _CONFIG_PATH=%2
IF "%_CONFIG_PATH%"=="" SET _CONFIG_PATH=D:\\logs\\application.properties

java -Dspring.config.location=%_CONFIG_PATH% -jar %_INSTALL_PATH%/resource-management-0.0.1-SNAPSHOT.jar