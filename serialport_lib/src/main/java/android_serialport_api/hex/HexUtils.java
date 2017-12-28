package android_serialport_api.hex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Created by Administrator on  2017/11/30
 * @version 1.0.
 */

public class HexUtils {

    public static String bytes2HexString(byte[] b) {
        StringBuilder result = new StringBuilder();
        String hex;
        for (byte aB : b) {
            hex = Integer.toHexString(aB & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase());
        }
        return result.toString();
    }


    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     * @Title:hexString2Bytes
     * @Description:16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer
                    .valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * @param strPart 字符串
     * @return 16进制字符串
     * @throws
     * @Title:string2HexString
     * @Description:字符串转16进制字符串
     */
    public static String string2HexString(String strPart) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();
    }

    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     * @Title:hexString2String
     * @Description:16进制字符串转字符串
     */
    public static String hexString2String(String src) {
        String temp = "";
        for (int i = 0; i < src.length() / 2; i++) {
            temp = temp
                    + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2),
                    16).byteValue();
        }
        return temp;
    }

    /**
     * @param src
     * @return
     * @throws
     * @Title:char2Byte
     * @Description:字符转成字节数据char-->integer-->byte
     */
    public static Byte char2Byte(Character src) {
        return Integer.valueOf((int) src).byteValue();
    }

    /**
     * @param a   转化数据
     * @param len 占用字节数
     * @return
     * @throws
     * @Title:intToHexString
     * @Description:10进制数字转成16进制
     */
    private static String intToHexString(int a, int len) {
        len <<= 1;
        String hexString = Integer.toHexString(a);
        int b = len - hexString.length();
        if (b > 0) {
            for (int i = 0; i < b; i++) {
                hexString = "0" + hexString;
            }
        }
        return hexString;
    }

    private static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * @param b 有符号的byte
     * @return unsigned  无符号值
     * byte b= -120;
     * int a= bytes & 0xff;
     */
    public static int signed2unsigned(byte b) {
        return b & 0xff;
    }

    /**
     * @param b 无符号的int
     * @return signed  有符号的byte
     * int a = 300;
     * byte c = (byte) a;
     */
    public static byte unsigned2signed(int b) {
        return (byte) b;
    }

    /**
     * List<Byte>转换为byte 数组
     *
     * @param list Byte集合
     * @return byte[]
     */
    public static byte[] listTobyte(List<Byte> list) {
        if (list == null || list.size() < 0)
            return null;
        byte[] bytes = new byte[list.size()];
        int i = 0;
        for (Byte aList : list) {
            bytes[i] = aList;
            i++;
        }
        return bytes;
    }

    /**
     * List<Byte>转换为byte 数组
     *
     * @param data byte[]
     * @return List<Byte>
     */
    public static List<Byte> byteToList(byte[] data) {
        if (data == null)
            return null;
        List<Byte> bytes = new ArrayList<>();
        for (Byte aList : data) {
            bytes.add(aList);
        }
        return bytes;
    }


    /**
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
     *
     * @param b 8位的byte数
     * @return 2进制byte的数组
     */
    public static byte[] getBooleanArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }


    private static int toByte(char c) {
        if (c >= '0' && c <= '9')
            return (c - '0');
        if (c >= 'A' && c <= 'F')
            return (c - 'A' + 10);
        if (c >= 'a' && c <= 'f')
            return (c - 'a' + 10);

        throw new RuntimeException("Invalid hex char '" + c + "'");
    }

    /**
     * 把byte转为字符串的bit
     *
     * @param b 8位的byte
     * @return 二进制字符串bit
     */
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }


    /**
     * 2个8位的byte数据转化为1个16位的short数据
     *
     * @param b1 数据1
     * @param b2 数据2
     * @return 16为的short整形数据
     * <pre>
     *      例如：
     *      short s = 0          // 一个16位整形变量，初值为 0000 0000 0000 0000
     *      byte b1 = 0x01;      //一个byte的变量，作为转换后的高8位，假设初值为 0000 0001
     *      byte b2 = 0x02;      //一个byte的变量，作为转换后的低8位，假设初值为 0000 0010
     *
     *      doubleByteToShort()后为0000 0001 0000 0010
     * </pre>
     */
    public static short doubleByteToShort(byte b1, byte b2) {
        short s = 0;            //一个16位整形变量，初值为 0000 0000 0000 0000
        s = (short) (s ^ b1);   //将b1赋给s的低8位
        s = (short) (s << 8);   //s的低8位移动到高8位
        s = (short) (s ^ b2);   //在b2赋给s的低8位l
        return s;
    }


    /**
     * 16位的short数字--->2个空间大小的8位byte数组
     *
     * @param i 16位的short数字
     * @return 2个空间大小的8位byte数组
     */
    public static byte[] toByteArray(short i) {
        return new byte[]{(byte) (i >> 8 & 0xff), (byte) (i & 0xff)};
    }

    /**
     * 32位的int数字--->4个空间大小的8位byte数组
     *
     * @param i 32位的short数字
     * @return 4个空间大小的8位byte数组
     */
    public static byte[] toByteArray(int i) {
        return new byte[]{(byte) (i >> 24 & 255), (byte) (i >> 16 & 255), (byte) (i >> 8 & 255), (byte) (i & 255)};
    }

}
