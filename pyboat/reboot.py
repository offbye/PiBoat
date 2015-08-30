#!/usr/bin/env python
# coding=utf-8
# author:ksc

import RPi.GPIO as GPIO
import time
import os,sys
import signal

GPIO.setmode(GPIO.BOARD)

#define GPIO pin
pin_btn=11
pin_led=13

GPIO.setup(pin_btn, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(pin_led, GPIO.OUT, initial=GPIO.LOW)

press_time=0
count_down=10
led_on=1

def cleanup():
    '''释放资源，不然下次运行是可能会收到警告
    '''
    print('clean up')
    GPIO.cleanup()

def handleSIGTERM(signum, frame):
    #cleanup()
    sys.exit()#raise an exception of type SystemExit

def onPress(channel):
    global press_time,count_down
    print('pressed')
    press_time+=1
    if press_time >3:
        press_time=1
    if press_time==1:
        GPIO.output(pin_led, 1)
        print('system will restart in %s'%(count_down))
    elif press_time==2:
        print('system will halt in %s'%(count_down))
    elif press_time==3:
        GPIO.output(pin_led, 0)
        print 'cancel '
        count_down=10

GPIO.add_event_detect(pin_btn, GPIO.FALLING, callback= onPress,bouncetime=500)

#signal.signal(signal.SIGTERM, handleSIGTERM)
try:
    while True:
        if press_time==1:
            if count_down==0:
                print "start restart"
                os.system("shutdown -r -t 5 now")
                sys.exit()
            led_on=not led_on
            GPIO.output(pin_led, led_on)# blink led
        if press_time==2 and count_down==0:
            print "start shutdown"
            os.system("shutdown  -t 5 now")
            sys.exit()
 
        if press_time==1 or press_time==2:
            count_down-=1
            print "%s second"%(count_down)
        time.sleep(1)
except KeyboardInterrupt:
    print('User press Ctrl+c ,exit;')
finally:
    cleanup()
