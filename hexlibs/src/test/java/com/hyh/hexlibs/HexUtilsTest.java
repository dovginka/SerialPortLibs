package com.hyh.hexlibs;

import org.junit.Test;

/**
 * @author Created by Administrator on  2018-03-19
 * @version 1.0.
 */
public class HexUtilsTest {

    @Test
    public void makeInt() throws Exception {
        byte b = -1;
        System.out.println(HexUtils.makeInt((byte) b, (byte) b));
        System.out.println(HexUtils.makeInt((byte) 1, (byte) 1));
        //257
    }

    @Test
    public void byteTolong() {
        String s = "0000000005DC";
        byte[] bytes = ByteUtils.hexStringToBytes(s);
        System.out.println(ByteUtils.bytesToLong(bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0], (byte) 0, (byte) 0));
    }

}