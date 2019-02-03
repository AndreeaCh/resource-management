SET _INSTALL_PATH=%1
IF "%_INSTALL_PATH%"=="" SET _INSTALL_PATH=target

java -jar %_INSTALL_PATH%/resource-management-0.0.1-SNAPSHOT.jar