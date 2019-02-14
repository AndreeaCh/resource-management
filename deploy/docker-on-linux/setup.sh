#!/bin/bash

INSTALL_PATH=$HOME/apps/easy-manage

LOGS_DIR=./logs
DATETIME=$(date +"%Y-%m-%d_%H-%M-%S")
LOGFILE=$LOGS_PATH/setup-$DATETIME.log

echo "SETUP_1.1 Load backend docker image"
docker load -i easy-manage-backend-latest.tar.gz

echo "SETUP_1.2 Load frontend docker image"
docker load -i easy-manage-frontend-latest.tar.gz

echo "SETUP_1.3 Load db docker image"
docker load -i easy-manage-db-latest.tar.gz

echo "SETUP_2.1 Install the application"
./install.sh $INSTALL_PATH 2>&1 > $LOGFILE

echo "SETUP_3.1 Start the application"
cd $INSTALL_PATH && ./start.sh 2>&1 > $LOGFILE
