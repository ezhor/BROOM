java -jar ./client/out/artifacts/client_jar/client.jar
nc -u -l 2222 | mplayer -fps 200 -demuxer h264es -