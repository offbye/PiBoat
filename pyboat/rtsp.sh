#/bin/sh
raspivid -o - -w 920 -h 540 -t 9999999 |cvlc -vvv stream:///dev/stdin --sout '#rtp{sdp=rtsp://:8554/}' :demux=h264
