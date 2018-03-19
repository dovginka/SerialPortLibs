package android.serialport.hex;

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

}