package android.serialport;

import org.junit.Test;

import java.io.FileInputStream;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
      //  assertEquals(4, 2 + 2);
        FileInputStream inputStream = new FileInputStream("C://log.txt");
        int a = inputStream.available();
        byte[] srcPos = new byte[1024];
        int len = inputStream.read(srcPos);
        byte[] destPos = new byte[len];
        System.arraycopy(srcPos, 0, destPos, 0, destPos.length);

        System.out.println("tess");

    }
}