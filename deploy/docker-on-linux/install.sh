#!/bin/bash

INSTALL_PATH=$1

echo "INSTALL_1 Install application to location '$INSTALL_PATH'"
tar -zxf readable-stream-1.0.*.tgz -C $INSTALL_PATH