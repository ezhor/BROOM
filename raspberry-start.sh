#!/bin/bash
echo "Raspberry Start" > raspberry.log
echo "Killing previous raspivid and nc executions..." > raspberry.log
killall raspivid
killall nc
echo "Killed OK" > raspberry.log
echo "Pulling from git..." > raspberry.log
git pull
echo "git pull OK" >> raspberry.log
echo "Opening netcat from camera" >> raspberry.log
(/opt/vc/bin/raspivid -t 0 -w 640 -h 360 -hf -fps 60 -o - | nc -lk -p 5001) &
echo "Net camera OK" >> raspberry.log
P1=$!
echo "Opening Python communication" >> raspberry.log
python ./raspberry/main.py &
echo "Python OK" >> raspberry.log
P2=$!
wait $P1 $P2
echo "Raspberry Finished" >> raspberry.log
