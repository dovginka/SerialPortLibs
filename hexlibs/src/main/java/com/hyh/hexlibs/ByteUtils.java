package com.hyh.hexlibs;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ByteUtils {

    private static final String HEX = "0123456789ABCDEF";

    public static byte charToByte(char c) {
        return (byte) HEX.indexOf(c);
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (ByteUtils.charToByte(hexChars[pos]) << 4 | ByteUtils.charToByte(
                    hexChars[pos + 1]));
        }
        return d;
    }

    /*将bytes数组转化为16进制字符串*/
    public static String bytesToHexString(byte[] bArray) {
        return bytesToHexString(bArray, bArray.length);
    }

    /*将bytes数组转化为16进制字符串*/
    public static String bytesToHexString(byte[] bArray, int len) {
        return bytesToHexString(bArray, 0, len);
    }

    /*将bytes数组转化为16进制字符串*/
    public static String bytesToHexString(byte[] bArray, int offset, int len) {
        if (bArray == null)
            return "";
        if (len > bArray.length)
            len = bArray.length;
        StringBuilder sb = new StringBuilder(len * 2);
        String sTemp;
        for (int i = offset; i < len; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static long bytesToLong(byte[] b) {
        if (b.length != 4) {
            return -1;
        }
        return bytesToLong(b[0], b[1], b[2], b[3]);
    }

    public static long bytesToLong(byte b1, byte b2, byte b3, byte b4) {
        return (b1 & 0xFF) | ((b2 & 0xFF) << 8) | ((b3 & 0xFF) << 8 * 2) | ((b4 & 0xFF) << 8 * 3);
    }

    public static long bytesToLong(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
        return (b1 & 0xFF) | ((b2 & 0xFF) << 8) | ((b3 & 0xFF) << 8 * 2) | ((b4 & 0xFF) << 8 * 3)
                | ((b5 & (long) 0xFF) << 8 * 4) | ((b6 & (long) 0xFF) << 8 * 4) | ((b7 & (long) 0xFF) << 8 * 4) | ((b8 & (long) 0xFF) << 8 * 4);
    }

    public static byte[] longToBytes(long l) {
        byte[] result = new byte[4];
        result[0] = (byte) (l & 0xFF);
        result[1] = (byte) (l >> 8 & 0xFF);
        result[2] = (byte) (l >> 16 & 0xFF);
        result[3] = (byte) (l >> 24 & 0xFF);
        return result;
    }

    public static int bytesToInt(byte b1, byte b2) {
        return (b1 & 0xFF) | ((b2 & 0xFF) << 8);
    }

    public static int bytesToInt(byte b1, byte b2, byte b3, byte b4) {
        return (b1 & 0xFF) | ((b2 & 0xFF) << 8) | ((b3 & 0xFF) << 16) | ((b4 & 0xFF) << 24);
    }

    /**
     * byte[]转int
     *
     * @param bytes 需要转换成int的数组
     * @return int值
     */
    public static int bytesToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * @param highBit 高八位
     * @param lowBit  低八位
     * @return 一个16位的Int数
     */
    public static int makeInt(byte highBit, byte lowBit) {
        return (((highBit & 0xff) << 8)) | (lowBit & 0xff);
    }

    /**
     * 32位的int数字--->4个空间大小的8位byte数组
     *
     * @param value 32位的short数字
     * @return 4个空间大小的8位byte数组
     */
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

//    public static void main(String[] args) {
//        byte[] bArray = intToBytes(10000000);
//        System.out.println(bytesToHexString(bArray));
//        System.out.println(bytesToInt(bArray[3], bArray[2], bArray[1], bArray[0]));
//        System.out.println(byteArrayToInt(bArray));
//    }


    // char[]转byte[]
    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);

        return bb.array();
    }

    public static byte getByte(char c) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(1);
        cb.put(c);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);

        byte[] tmp = bb.array();
        return tmp[0];
    }

    // byte转char
    public static char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);

        return cb.array();
    }

    // byte转char
    public static char getChar(byte bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(1);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);

        char[] tmp = cb.array();

        return tmp[0];
    }

    /**
     * 字符串转字节
     *
     * @param s 字符串
     * @return 字节
     */
    public static byte[] stringToBytes(String s) {
        return stringToBytes(s, 0);
    }

    /**
     * 字符串转字节
     *
     * @param s      字符串
     * @param length 字节长度，不够补0
     */
    public static byte[] stringToBytes(String s, int length) {
        byte[] src =
                ByteUtils.getBytes((s == null || s.length() == 0) ? new char[0] : s.toCharArray());

        if (length < src.length) {
            length = src.length;
        }

        byte[] bytes = new byte[length];
        for (int i = 0; i < src.length; i++) {
            bytes[i] = src[i];
        }
        return bytes;
    }

    public static byte[] copy(byte[] src, int srcStart, int length) {
        byte[] result = new byte[length];
        System.arraycopy(src, srcStart, result, 0, length);
        return result;
    }

    public static byte[] reverse(byte[] src) {
        for (int i = 0; i <= src.length / 2 - 1; i++) {
            byte temp1 = src[i];
            byte temp2 = src[src.length - i - 1];
            src[i] = temp2;
            src[src.length - i - 1] = temp1;
        }
        return src;
    }

    public static String bytesToAscii(byte[] bytes, int offset, int dateLen) {
        if ((bytes == null) || (bytes.length == 0) || (offset < 0) || (dateLen <= 0)) {
            return null;
        }
        if ((offset >= bytes.length) || (bytes.length - offset < dateLen)) {
            return null;
        }

        String asciiStr = null;
        byte[] data = new byte[dateLen];
        System.arraycopy(bytes, offset, data, 0, dateLen);
        try {
            asciiStr = new String(data, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
        }
        return asciiStr;
    }

    public static String bytesToAscii(byte[] bytes, int dateLen) {
        return bytesToAscii(bytes, 0, dateLen);
    }

    public static String bytesToAscii(byte[] bytes) {
        return bytesToAscii(bytes, 0, bytes.length);
    }

    /**
     * 字符串 转 ascii bytes
     * ISO-8859-1 码表兼容大部分码表，因此适合做中间码表
     */
    public static byte[] stringToAsciiBytes(String s) {
        try {
            return s.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


    /**
     * 将16进制单精度浮点型转换为10进制浮点型
     *
     * @return float
     * @since 1.0
     */
    private float parseHex2Float(String hexStr) {
        BigInteger bigInteger = new BigInteger(hexStr, 16);
        return Float.intBitsToFloat(bigInteger.intValue());
    }

    /**
     * 将十进制浮点型转换为十六进制浮点型
     *
     * @return String
     * @since 1.0
     */
    private String parseFloat2Hex(float data) {
        return Integer.toHexString(Float.floatToIntBits(data));
    }


    public static String hex4digits(String id) {
        if (id.length() == 1) return "000" + id;
        if (id.length() == 2) return "00" + id;
        if (id.length() == 3) return "0" + id;
        else return id;
    }

    public static byte[] toArray(List<Byte> data) {
        byte[] bytes = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            bytes[i] = data.get(i);
        }
        return bytes;
    }

    public static List<Byte> toList(byte[] data) {
        ArrayList<Byte> bytes = new ArrayList<>(data.length);
        for (byte datum : data) {
            bytes.add(datum);
        }
        return bytes;
    }

}
