package com.hyh.hexlibs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CRC16Test {
    @Test
    public void testCrcTable() {
        //d382
        byte[] data = {0x00, 0x06, 0x00, 0x3a};
        String x = CRC16.crcTable(data);
        char[] c = new char[25];
        byte[] bytes = x.getBytes();
        byte[] bytes1 = ByteUtils.hexStringToBytes(x);
        Integer b = 0xc181;
        byte b1 = b.byteValue();
        byte length = 0x04;
        Integer a = 0x04;
        byte b2 = a.byteValue();
        System.out.println(x);
    }

}
