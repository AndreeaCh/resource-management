#!/bin/bash

ARCHIVE_PATH=$1
INSTALL_PATH=$2
SERVER_ADDRESS=${3:-localhost}

echo "INSTALL_0 Create installation directories if necessary '$INSTALL_PATH'"
mkdir -p $INSTALL_PATH

echo "INSTALL_1 Install application to location '$INSTALL_PATH'"
tar -xf $ARCHIVE_PATH -C $INSTALL_PATH --strip 1

echo "INSTALL_2 Write config file"

echo "INSTALL_PATH=$INSTALL_PATH" > ${HOME}/.easyman
echo "SERVER_ADDRESS=$SERVER_ADDRESS" >> ${HOME}/.easyman
echo "FRONTEND_RAM=1024000000" >> ${HOME}/.easyman
echo "BACKEND_RAM=1024000000" >> ${HOME}/.easyman
echo "DB_RAM=1024000000" >> ${HOME}/.easyman

echo "INSTALL_3 Add bin to PATH"

sed -i.bak '/export EASYMAN_HOME=/d' ${HOME}/.bashrc
sed -i.bak '/PATH=$EASYMAN_HOME:$PATH/d' ${HOME}/.bashrc
echo "export EASYMAN_HOME=$INSTALL_PATH" >> ${HOME}/.bashrc
echo "PATH=\$EASYMAN_HOME:\$PATH" >> ${HOME}/.bashrc