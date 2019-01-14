#!/bin/sh

LOG_DIR=~/logs
IMPORT_DIR=~/import

import_data ()
{
    for i in `seq 0 10`;
    do
        echo "Adding element number: $i"
        mongoimport -d test -c subUnit --file $IMPORT_DIR/subUnit$i.json
    done

    for i in `seq 0 2`;
    do
        echo "Adding service number: $i"
        mongoimport -d test -c service --file $IMPORT_DIR/service$i.json
    done
}

DATE_WITH_TIME=`date "+%Y%m%d-%H%M%S"`
nohup mongod > $LOG_DIR/mongod-$DATE_WITH_TIME.log 2>&1 &

echo "Waiting for mongodb to start"
sleep 10

if [ ! -z $1 ]
then
    echo "Demo mode is on. Importing data."
    import_data
fi

tail -f $LOG_DIR/mongod-$DATE_WITH_TIME.log
