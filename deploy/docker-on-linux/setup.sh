#!/bin/bash

SERVER_ADDRESS=$1
INSTALL_PATH=$HOME/apps/easy-manage

IMAGES_DIR=./images
DIST_DIR=./dist
LOGS_DIR=./logs

DATETIME=$(date +"%Y-%m-%d_%H-%M-%S")
LOGFILE=$LOGS_DIR/setup-$DATETIME.log

echo "SETUP_1/5 Load backend docker image"
docker load -i $IMAGES_DIR/backend-docker-image.tar.gz

echo "SETUP_2/5 Load frontend docker image"
docker load -i $IMAGES_DIR/frontend-docker-image.tar.gz

echo "SETUP_3/5 Load db docker image"
docker load -i $IMAGES_DIR/mongodb-docker-image.tar.gz

echo "SETUP_4/5 Load auth docker image"
docker load -i $IMAGES_DIR/auth-docker-image.tar.gz

echo "SETUP_5/5 Install the application"
./install.sh $DIST_DIR/easy-manage-docker-on-linux-bin.tar.gz $INSTALL_PATH $SERVER_ADDRESS 2>&1 > $LOGFILE

echo "Application installed successfully. Run 'easyman' in a new shell for available options"