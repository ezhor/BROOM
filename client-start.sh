git pull
java --module-path /usr/share/openjfx/lib --add-modules=javafx.controls,javafx.fxml,javafx.base,javafx.media,javafx.web,javafx.swing -jar ./client/out/artifacts/client_jar/client.jar
nc -u -l 2222 | mplayer -fps 200 -demuxer h264es -