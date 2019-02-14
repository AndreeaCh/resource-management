#!/bin/bash

# TODO : INSTALL_PATH resolved at runtime
INSTALL_PATH=$HOME/apps/easy-manage

echo "UNINSTALL_0 Uninstall the application at the location '$INSTALL_PATH'"

echo "UNINSTALL_1 Stop any running container"
docker rm -f $(docker ps -a -q)

echo "UNINSTALL_1 Remove images"
docker rmi -f $(docker images -q "isu/easy-manage-service-*")

echo "UNINSTALL_X Remove application files"
rm -rf $INSTALL_PATH/
