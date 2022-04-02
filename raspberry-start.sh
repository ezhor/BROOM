#!/bin/bash
echo "Raspberry Start" > raspberry.log

echo "Pulling from git..." > raspberry.log
git pull
echo "git pull OK" >> raspberry.log

/bin/bash ./raspberry/raspberry-run.sh