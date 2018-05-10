# SerialPortLibs
序列化串口封装库（包含源码）



# so相关问题

### 问题1

>java.lang.UnsatisfiedLinkError: dlopen failed: /data/app/com.shizhi.usp-1/lib/arm/libserial_port.so: has text relocations

> 在Android 6.0之前, text reloactions问题, 会在编译的过程中, 作为warning报出来, log大致如下:ActivityManager: WARNING: linker: libdvm.so has text relocations. This is wasting memory and is a security risk. >Please fix. ? ? 在Android 6.0中, 原来的warning被升级为error了. 因此, 同样的库文件, 在Android 6.0前的环境运行, 不报错, 6.0下就会crash掉.

> 解决方案：
> 
> [http://www.dailibu.com/rengongzhineng/2016056002/TEXTRELWenTiDeGuanFangHuiFu](http://www.dailibu.com/rengongzhineng/2016056002/TEXTRELWenTiDeGuanFangHuiFu "TEXTREL问题的官方回复")

>[https://code.google.com/archive/p/android-developer-preview/issues/3028](https://code.google.com/archive/p/android-developer-preview/issues/3028 "Google回复")