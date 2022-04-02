#!/bin/bash
echo "Relay Start" > raspberry.log

echo "Pulling from git..." > raspberry.log
git pull
echo "git pull OK" >> raspberry.log

/bin/bash ./relay/relay-run.sh