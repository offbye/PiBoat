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
        GPIO.setmode(GPIO.BOARD)
        GPIO.setup(12, GPIO.OUT)
        GPIO.setup(18, GPIO.OUT)

        self.m1 = GPIO.PWM(12, 50)
        self.s1 = GPIO.PWM(18, 50)

        self.m1.start(0)
        self.s1.start(0)
        self.isM1start = True
        self.isS1start = True

        time.sleep(3)
        self.m1.ChangeDutyCycle(5)
        #self.s1.ChangeDutyCycle(5)
        time.sleep(1)

    def stop(self):
        self.m1.stop()
        self.s1.stop()
        GPIO.cleanup()

    def start(self):
        self.m1.start(0)
        time.sleep(0.02)
        self.m1.ChangeDutyCycle(5)
        time.sleep(0.02)

    def motor_set(self, percent):
        if percent == 0:
            self.m1.ChangeDutyCycle(0)
            time.sleep(0.05)
            self.m1.stop()
            self.isM1start = False
            print("stop servo1")
        elif 0 < percent < 100:
            if self.isM1start == False:
                self.m1 = GPIO.PWM(12, 50)
                self.m1.start(0)
                time.sleep(0.05)
                self.isM1start = True
            speed_percent = 5 + 5 * (percent / 100)
            print(speed_percent)
            self.m1.ChangeDutyCycle(speed_percent)
            print("motor_set: " + str(speed_percent))

            time.sleep(0.05)
            return "ok"
        else:
            return "invalid percent"

    def servo1_set(self, percent):
        if percent == 0:
            self.s1.ChangeDutyCycle(0)
            time.sleep(0.05)
            self.s1.stop()
            self.isS1start = False
            time.sleep(0.05)
            print("stop servo1")

            self.m1.ChangeDutyCycle(0)
            time.sleep(0.05)
            self.m1.stop()
            self.isM1start = False
            time.sleep(0.05)
            print("stop motor1")
        elif 0 < percent < 100:
            if self.isS1start == False:
                self.s1 = GPIO.PWM(18, 50)
                self.s1.start(0)
                time.sleep(0.05)
                self.isS1start = True
            speed_percent = 3.2 + 8 * (percent / 100)
            self.s1.ChangeDutyCycle(speed_percent)
            time.sleep(0.05)
            print("servo1_set: " + str(speed_percent))
            return "ok"
        else:
            return "invalid percent"

if __name__ == "__main__":
    pwm1 = PiPWM()
    try:
        while 1:
            time.sleep(1)
            i = float(raw_input("speed?(5-10)"))
            pwm1.servo1_set(i)
            print("------" + str(i))
    except KeyboardInterrupt:
        pass
    pwm1.stop()
