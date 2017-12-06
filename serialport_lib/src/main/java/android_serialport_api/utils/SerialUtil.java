package android_serialport_api.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPort;

/**
 * 单例模式下的串口通信类
 * Created by Administrator on 2016/8/26.
 */
public enum SerialUtil {
    INIT;
    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    protected byte[] buffer;

    /**
     * @param path     路径
     * @param baudrate 波特率
     * @param flags    标志位
     * @throws NullPointerException 有错误的话，抛出异常
     */
    public void init(String path, int baudrate, int flags) throws Exception {
        try {
            serialPort = new SerialPort(new File(path), baudrate, flags);
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
        } catch (Exception e) {
            throw new Exception("串口设置有误");
        }
    }


    /**
     * 串口读数据,直接把Byte类型转成String类型
     *
     * @return 直接返回String
     */
    public synchronized String getData() throws Exception {
        //上锁，每次只能一个线程在取得数据
        byte[] dataByte = getDataByte();
        if (dataByte == null)
            return "";
        else
            return new String(dataByte, Charset.forName("UTF-8"));
    }

    /**
     * @return byte[] 返回一个接收的数据
     * @throws NullPointerException 如果inputStream为空，就抛出异常
     */
    public byte[] getDataByte() throws Exception {
        if (inputStream == null) throw new NullPointerException(" inputStream is null");
        int size = inputStream.available();
        if (size > 0) {
            buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            inputStream.read(buffer);
            return buffer;
        } else
            return null;
    }

    private List<Byte> data;

    public byte[] getDataByte2() throws Exception {
        if (inputStream == null) throw new NullPointerException(" inputStream is null");
        buffer = new byte[512];
        data = new ArrayList<>();
        int len = -1;
        while ((len = inputStream.read(buffer)) > 0) {
            for (int i = 0; i < len; i++) {
                data.add(buffer[i]);
            }
        }
        if (data.size() == 0) {
            return null;
        } else {
            buffer = new byte[data.size()];
            for (int i = 0; i < data.size(); i++) {
                buffer[i] = data.get(i);
            }
            data.clear();
            return buffer;
        }
    }

    /**
     * 发送数据
     *
     * @param bytes 显示的16进制的字符串
     */
    public void setData(byte[] bytes, int start, int len) throws Exception {
        if (outputStream == null) throw new NullPointerException("outputStream is null");
        outputStream.write(bytes, start, len);
        outputStream.flush();
    }


    public void setData(int[] res) throws IOException {
        for (int re : res) {
            outputStream.write(re);
        }
        outputStream.flush();
    }

    /**
     * 关闭串口
     */
    public void closeSerialport() {
        if (serialPort != null) {
            serialPort.close();
            serialPort = null;
        }
        close(outputStream);
        close(inputStream);
    }

    private void close(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
            closeable = null;
        } catch (IOException e) {//
        }
    }

}
