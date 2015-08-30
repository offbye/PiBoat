#!/usr/bin/python
# -*- encoding: UTF-8 -*-
# SockBoatServer created on 15/8/30 下午3:49
# Copyright 2014 offbye@gmail.com


"""

"""

__author__ = ['"Xitao":<offbye@gmail.com>']

from SocketServer import ThreadingTCPServer, StreamRequestHandler
import traceback
import threading
import os,sys
from pi_pwm import  PiPWM


timer_interval = 2


class MyStreamRequestHandlerr(StreamRequestHandler):
    def handle(self):
        # t = threading.Timer(5.0, self.sayhello)
        # t.start()

        while True:
            try:
                data = self.rfile.readline().strip()
                if data != '':
                    print "receive from (%r):%r" % (self.client_address, data)
                    self.wfile.write(data.upper())
                if data == "gps":
                    self.wfile.write(get_gps())
                elif data == "reboot":
                    os.system('reboot')
                    sys.exit()
                elif data == "halt":
                    os.system("shutdown -r -t 5 now")
                    sys.exit()
                elif data == "rtsp":
                    os.system("raspivid -o - -w 640 -h 360 -t 9999999 |cvlc -vvv stream:///dev/stdin --sout '#rtp{sdp=rtsp://:8554/}' :demux=h264 & ")
                elif data[0:2] == "m1":
                    print("---" + data.upper())
                    pwm.motor_set(float(data.split(",")[1]))

                elif data[0:2] == "s1":
                    print("---" + data.upper())
                    pwm.servo1_set(float(data.split(",")[1]))

            except:
                traceback.print_exc()
                self.finish()
                break


    def sayhello(self):
        print "hello"
        self.wfile.write("hello")
        global t        #Notice: use global variable!
        t = threading.Timer(2.0, self.sayhello)
        t.start()


def get_gps():
    print "-----gps"
    return "lat,lat"


if __name__ == "__main__":
    host = ""  # 主机名，可以是ip,像localhost的主机名,或""
    port = 9999  # 端口
    addr = (host, port)
    try:
        pwm = PiPWM()

        # ThreadingTCPServer从ThreadingMixIn和TCPServer继承
        #class ThreadingTCPServer(ThreadingMixIn, TCPServer): pass
        server = ThreadingTCPServer(addr, MyStreamRequestHandlerr)
        server.serve_forever()
    except KeyboardInterrupt:
        pass

    server.server_close()
    pwm.stop()
