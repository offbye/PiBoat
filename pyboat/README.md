#PiBoat (Respberry Pi Boat)
 
PiBoat is a hackathon project in Nanjing 2015 Sponsor by Qianmi.com. This project finished in 24 hours by 3 engineers. 


#1. RTSP Video Streaming

* Use Respberry Pi Camera Module capture video stream

* Use VLC provide RTSP Streaming

`raspivid -o - -w 920 -h 540 -t 9999999 |cvlc -vvv stream:///dev/stdin --sout '#rtp{sdp=rtsp://:8554/}' :demux=h264 `

You can play the Stream use VLC client, the address is

rtsp://192.168.1.101:8554


#2. ESC (Eletronic Speed Control) control

Use Python and RPi.GPIO pwm control ESC
 
#3. Servo control  

Use Python and RPi.GPIO pwm control servo

#4. GPS control 

Not finished, use serial gather GEO inof from a Ublox GPS 6M module.

#5. Android App

Use Socket connect to Respberry Pi, use Vitamio play video stream.


The project is open source under Apache Lincinse, you play with it feel free.


 

  
  
