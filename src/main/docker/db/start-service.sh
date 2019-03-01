#!/bin/sh

LOG_DIR=~/logs
IMPORT_DIR=~/import

import_data ()
{
    for entry in "$IMPORT_DIR"/subUnit*\.json
	do
  		echo "Adding subunit: $entry"
        mongoimport -d test -c subUnit --file $entry
	done

    for entry in "$IMPORT_DIR"/service*\.json
    do
        echo "Adding service: $entry"
        mongoimport -d test -c service --file $entry
    done
}

DATE_WITH_TIME=`date "+%Y%m%d-%H%M%S"`
nohup mongod > $LOG_DIR/mongod-$DATE_WITH_TIME.log 2>&1 &

echo "Waiting for mongodb to start"
sleep 10

echo "Importing data if available"
import_data

tail -f $LOG_DIR/mongod-$DATE_WITH_TIME.log
