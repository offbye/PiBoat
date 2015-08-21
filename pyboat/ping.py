#!/usr/bin/python2.7
# -*- encoding: UTF-8 -*-
# ping created on 15/8/21 下午9:03 
# Copyright 2014 offbye@gmail.com


"""

"""

__author__ = ['"Xitao":<offbye@gmail.com>']

import os


for i in range(1, 6):
    ret = os.system('ping -c 1 -W 1 192.168.43.%d &> /dev/null' % i)
    if not ret:
        print 'ping 192.168.43.%d ok' % i