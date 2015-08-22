#!/usr/bin/python2.7
# -*- encoding: UTF-8 -*-
# gps_test created on 15/8/22 上午12:44 
# Copyright 2014 offbye@gmail.com


"""

"""

__author__ = ['"Xitao":<offbye@gmail.com>']

import gps3

gps_connection = gps3.GPSDSocket()
gps_fix = gps3.Fix()

try:
    for new_data in gps_connection:
        if new_data:
            gps_fix.refresh(new_data)
            print(gps_fix.TPV['time'])
            print(gps_fix.TPV['lat'])
            print(gps_fix.TPV['lon'])
            print(gps_fix.SKY['gdop'])
except KeyboardInterrupt:
    gps_connection.close()
    print('\nTerminated by user\nGood Bye.\n')