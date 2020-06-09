package android.serialport;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 单例模式下的串口通信类
 * Created by Administrator on 2016/8/26.
 */
public class SerialPortHandler implements SerialInterface {

    private SerialPort serialPort = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    @Override
    public void open(String path, int baudrate, int flags) throws Exception {
        serialPort = new SerialPort(new File(path), baudrate, flags);
        //设置读、写
        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        return inputStream.read(buffer);
    }

    @Override
    public int read(byte[] buffer, int off, int len) throws IOException {
        return inputStream.read(buffer, off, len);
    }

    @Override
    public int write(byte[] buffer) throws IOException {
        outputStream.write(buffer);
        outputStream.flush();
        return buffer.length;
    }

    @Override
    public int write(byte[] buffer, int off, int len) throws IOException {
        outputStream.write(buffer, off, len);
        outputStream.flush();
        return buffer.length;
    }

    @Override
    public void closeSerialPort() {
        if (serialPort != null) {
            //flush();
            serialPort.release();
            serialPort.close();
            serialPort = null;
        }
        closeIo(outputStream);
        closeIo(inputStream);
    }

    private void closeIo(Closeable closeable) {
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
