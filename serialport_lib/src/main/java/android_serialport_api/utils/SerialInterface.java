package android_serialport_api.utils;

import java.io.Closeable;

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
    void init(String path, int baudrate, int flags) throws Exception;

    /**
     * 串口读数据,直接把Byte类型转成String类型
     *
     * @return 直接返回String
     */
    String getData() throws Exception;

    /**
     * @return byte[] 返回一个接收的数据
     * @throws NullPointerException 如果inputStream为空，就抛出异常
     */
    byte[] getDataByte() throws Exception;

    /**
     * 发送数据
     *
     * @param data 显示的16进制的字符串
     */
    void setData(byte[] data) throws Exception;

    void setData(byte[] data, int start, int len) throws Exception;

    void setData(int[] res) throws Exception;

    /**
     * 关闭串口
     */
    void closeSerialPort();

    void closeIo(Closeable closeable);
}
