#!/bin/bash

ARCHIVE_PATH=$1
INSTALL_PATH=$2

echo "INSTALL_0 Create installation directories if necessary '$INSTALL_PATH'"
mkdir -p $INSTALL_PATH

echo "INSTALL_1 Install application to location '$INSTALL_PATH'"
tar -xf $ARCHIVE_PATH -C $INSTALL_PATH --strip 1