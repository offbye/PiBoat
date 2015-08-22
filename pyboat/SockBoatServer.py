#!/usr/bin/python2.7
# -*- encoding: UTF-8 -*-
# SockBoatServer created on 15/8/21 下午3:49 
# Copyright 2014 offbye@gmail.com


"""

"""

__author__ = ['"Xitao":<offbye@gmail.com>']

from SocketServer import ThreadingTCPServer, StreamRequestHandler
import traceback
import threading
from pi_pwm import  PiPWM


timer_interval = 2


class MyStreamRequestHandlerr(StreamRequestHandler):
    def handle(self):
        # t = threading.Timer(5.0, self.sayhello)
        # t.start()

        while True:
            try:
                data = self.rfile.readline().strip()
                print "receive from (%r):%r" % (self.client_address, data)
                self.wfile.write(data.upper())
                if data == "gps":
                    self.wfile.write(get_gps())
                elif data[0:2] == "m1":
                    print("---" + data.upper())
                    pwm.motor_set(float(data.split(",")[1]))

                elif data[0:2] == "s1":
                    print("---" + data.upper())
                    pwm.servo1_set(float(data.split(",")[1]))

            except:
                traceback.print_exc()
                break


    def sayhello(self):
        print "hello world"
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

    pwm = PiPWM()

    # ThreadingTCPServer从ThreadingMixIn和TCPServer继承
    #class ThreadingTCPServer(ThreadingMixIn, TCPServer): pass
    server = ThreadingTCPServer(addr, MyStreamRequestHandlerr)
    server.serve_forever()

    pwm.stop()