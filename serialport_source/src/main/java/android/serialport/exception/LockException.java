package android.serialport.exception;

import java.io.IOException;

/**
 * @author Created by Administrator on  2018-03-01
 * @version 1.0.
 * @desc 进程锁异常
 */

public class LockException extends IOException {
    public LockException() {
        super();
    }

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockException(Throwable cause) {
        super(cause);
    }
}
