#!/bin/bash
host
dir

while getopts ":h:d:c:" opt
do
    case $opt in
        h)
        host=$OPTARG
        echo "Host: $OPTARG"
        ;;
        d)
        dir=$OPTARG
        echo "Dir:$OPTARG"
        ;;

    esac
done

echo $host
echo $dir

#mvn clean
#mvn install

java -jar target/test-project-1.0-SNAPSHOT.jar du qing xiang $host $dir


