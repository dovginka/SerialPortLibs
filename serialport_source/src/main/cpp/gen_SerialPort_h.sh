#!/bin/sh
#根据java的Jni的调用类生产.h头文件
#javah -o SerialPort.h -jni -classpath ../java android.serialport.SerialPort
javah -o SerialPort.h -jni -classpath ../java com.nativec.tools.SerialPort

