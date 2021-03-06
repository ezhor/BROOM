#!/bin/bash
echo "Raspberry Start" > raspberry.log

echo "Freeing ports..." > raspberry.log
killall raspivid
killall nc
fuser -k 2727/tcp
echo "Killed OK" > raspberry.log

echo "Pulling from git..." > raspberry.log
git pull
echo "git pull OK" >> raspberry.log

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
