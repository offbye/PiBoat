#!/usr/bin/python
# -*- encoding: UTF-8 -*-
# pi_pwm created on 15/8/21 下午4:01 
# Copyright 2015 offbye@gmail.com


"""
Use PWM contoll ESC motor and Servo
Brushless Motor 1 ESC connected to GPIO 12, Servo 1 connect to GPIO 14

"""

__author__ = ['"Xitao":<offbye@gmail.com>']

import time
import RPi.GPIO as GPIO

if __name__ == "__main__":
    speed = 5.0


class PiPWM():
    def __init__(self):
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(12, GPIO.OUT)
        GPIO.setup(18, GPIO.BCM)
        GPIO.setup(16, GPIO.OUT)

        self.m1 = GPIO.PWM(12, 50)
        self.s1 = GPIO.PWM(18, 50)
        self.s2 = GPIO.PWM(16, 50)

        self.m1.start(0)
        time.sleep(3)
        self.m1.ChangeDutyCycle(5)
        time.sleep(2)
        self.m1.ChangeDutyCycle(6)


    def stop(self):
        self.m1.stop()
        self.s1.stop()
        GPIO.cleanup()

    def start(self, m1):
        m1.start(0)
        time.sleep(5)
        m1.ChangeDutyCycle(5)
        time.sleep(5)
        m1.ChangeDutyCycle(9)
        time.sleep(5)

    def motor_set(self, percent):
        if 0 < percent < 100:
            speed_percent = 5 + 5 * (percent / 100)
            self.m1.ChangeDutyCycle(speed_percent)
            time.sleep(0.02)
            return "ok"
        else:
            return "invalid percent"

    def servo1_set(self, percent):
        if 0 < percent < 100:
            speed_percent = 5 + 5 * (percent / 100)
            self.s1.ChangeDutyCycle(speed_percent)
            time.sleep(0.02)
            return "ok"
        else:
            return "invalid percent"

if __name__ == "__main__":
    pwm1 = PiPWM()
    try:
        while 1:
            time.sleep(5)
            i = float(raw_input("speed?(5-10)"))
            pwm1.servo1_set(i)
            print("------" + str(i))
    except KeyboardInterrupt:
        pass
    pwm1.stop()