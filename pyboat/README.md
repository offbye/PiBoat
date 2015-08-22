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


# Usage

* Install a lastest Respbian System, we use 2015-05-05-raspbian-wheezy.img, and start it success.

* Connect ESC signal line to GPIO12, Ground line to Pi GPIO6
* Connect Servo signal line  to GPIO18
* Connect  Respberry Pi Camera Module  ，then run `sudo apt-get install vlc`

* run 'sodu python SockBoatServer.py ' start the socket server.
* run Android app and controll the boat.

The project is open source under Apache Lincinse, you play with it feel free.



 

  
  
