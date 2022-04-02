#!/bin/bash

echo "Freeing ports..." > raspberry.log
killall raspivid
killall nc
fuser -k 2727/tcp
fuser -k 2727/udp
fuser -k 5001/tcp
fuser -k 5001/udp
echo "Killed OK" > raspberry.log

echo "Starting video streaming..." >> raspberry.log
(/opt/vc/bin/raspivid -n -ih -t 0 -rot 0 -w 512 -h 288 -b 1000000 -fps 60 -o - | nc -lk4 5001) &
echo "Video streaming OK" >> raspberry.log
P1=$!

echo "Opening Python communication" >> raspberry.log
python ./raspberry/main.py &
echo "Python OK" >> raspberry.log
P2=$!

wait $P1 $P2
echo "Raspberry Finished" >> raspberry.log
