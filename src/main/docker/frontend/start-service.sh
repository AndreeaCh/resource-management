#!/bin/sh

LOG_DIR=~/logs
APP_DIR=~/bin

DATE_WITH_TIME=`date "+%Y%m%d-%H%M%S"`

# modify backend server address for the frontend to connect to
sed -i 's/localhost/${SERVER_ADDRESS}/g' $APP_DIR/dist/static/env.js

http-server $APP_DIR/dist -p 8080
# TODO: redirect log > $LOG_DIR/frontend-$DATE_WITH_TIME.log 2>&1
