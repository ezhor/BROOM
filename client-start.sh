git pull
./netcat/nc64.exe -u -l 2222 | "C:/Program Files/VideoLAN/VLC/vlc.exe" - &
P1=$!
java --module-path /usr/share/openjfx/lib --add-modules=javafx.controls,javafx.fxml,javafx.base,javafx.media,javafx.web,javafx.swing -jar ./client/out/artifacts/client_jar/client.jar &
P2=$!
wait $P1 $P2
fuser -k 2727/tcp