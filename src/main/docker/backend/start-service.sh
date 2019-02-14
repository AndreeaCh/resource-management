#!/bin/sh

LOG_DIR=~/logs
APP_DIR=~/bin

DATE_WITH_TIME=`date "+%Y%m%d-%H%M%S"`

java -jar $APP_DIR/easy-manage.jar
# TODO: redirect log > $LOG_DIR/backend-$DATE_WITH_TIME.log 2>&1
