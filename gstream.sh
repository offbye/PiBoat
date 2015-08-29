#!/bin/sh

raspivid -t 0 -w 800 -h 600 -fps 25 -g 5 -b 4000000 -vf -n -o - | gst-launch -v fdsrc ! h264parse ! gdppay ! tcpserversink host=127.0.0.1 port=5000
/home/pi/gst-rtsp-0.10.8/exemples/test-launch
