git pull
(/opt/vc/bin/raspivid -t 0 -w 1280 -h 720 -hf -fps 20 -o - | nc -u 192.168.0.15 2222) &
nc -u -l 2222 | mplayer -noconsolecontrols -fps 200 -demuxer h264es - &
P1=$!
python ./raspberry/main.py &
P2=$!
wait $P1 $P2

