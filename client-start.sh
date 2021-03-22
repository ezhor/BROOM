git pull
nc -u -l 2222 | mplayer -noconsolecontrols -fps 200 -demuxer h264es - &
P1=$!
java --module-path /usr/share/openjfx/lib --add-modules=javafx.controls,javafx.fxml,javafx.base,javafx.media,javafx.web,javafx.swing -jar ./client/out/artifacts/client_jar/client.jar &
P2=$!
wait $P1 $P2
fuser -k 2727/tcp