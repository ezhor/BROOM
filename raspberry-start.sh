git pull
(/opt/vc/bin/raspivid -t 0 -w 640 -h 460 -hf -fps 20 -o - | nc -u 192.168.0.15 2222) &
P1=$!
python ./raspberry/main.py &
P2=$!
wait $P1 $P2

