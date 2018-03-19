package android.serialport.utils;

import android.serialport.SerialPort;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;


/**
 * 单例模式下的串口通信类
 * Created by Administrator on 2016/8/26.
 */
public enum SerialUtil implements SerialInterface {
    INIT;

    private SerialUtil() {

    }

    private SerialPort serialPort = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    @Override
    public void init(String path, int baudrate, int flags) throws Exception {
        serialPort = new SerialPort(new File(path), baudrate, flags);
        //设置读、写
        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();
    }

    @Override
    public String getData() throws Exception {
        //上锁，每次只能一个线程在取得数据
        byte[] dataByte = getDataByte();
        if (dataByte == null)
            return "";
        else
            return new String(dataByte, Charset.forName("UTF-8"));
    }

    @Override
    public byte[] getDataByte() throws Exception {
        /*if (inputStream == null) throw new NullPointerException(" inputStream is null");
        byte[] srcPos = new byte[126];
        int len = inputStream.read(srcPos);
        byte[] destPos = new byte[len];
        System.arraycopy(srcPos, 0, destPos, 0, destPos.length);
        return destPos;*/
        int size = inputStream.available();
        if (size > 0) {
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            inputStream.read(buffer);
            return buffer;
        } else
            return null;
    }

    @Override
    public void setData(byte[] data) throws Exception {
        setData(data, 0, data.length);
    }

    @Override
    public void setData(byte[] data, int start, int len) throws Exception {
        if (outputStream == null) throw new NullPointerException("outputStream is null");
        outputStream.write(data, start, len);
        outputStream.flush();
    }

    @Override
    public void setData(int[] res) throws IOException {
        for (int re : res) {
            outputStream.write(re);
        }
        outputStream.flush();
    }

    @Override
    public void closeSerialPort() {
        if (serialPort != null) {
            serialPort.release();
            serialPort.close();
            serialPort = null;
        }
        closeIo(outputStream);
        closeIo(inputStream);
    }

    @Override
    public void closeIo(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
            closeable = null;
        } catch (IOException e) {//
        }
    }

    @Override
    public void flush() {
        if (serialPort != null) {
            serialPort.tcflush();
        }
    }
}
