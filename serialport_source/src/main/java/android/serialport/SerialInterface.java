package android.serialport;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Created by Administrator on  2018-02-02
 * @version 1.0.
 */

public interface SerialInterface {


    /**
     * @param path     串口号
     * @param baudrate 波特率
     * @param flags    数据位
     * @throws Exception
     */
    void open(String path, int baudrate, int flags) throws Exception;

    int read(byte[] buffer) throws IOException;

    int read(byte[] buffer, int off, int len) throws IOException;

    int write(byte[] buffer) throws IOException;

    int write(byte[] buffer, int off, int len) throws IOException;

    /**
     * 关闭串口
     */
    void closeSerialPort();

    /**
     * 清除缓存
     */
    void flush();
}
