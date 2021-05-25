package android.serialport;

import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * @author Created by Administrator on  2017/12/1
 * @version 1.0.
 */

public class SerialIoManager extends Thread {

    private static final String TAG = "SerialIoManager";
    private static final int BUF_SIZE = 1024 * 4;
    // Synchronized by 'mWriteBuffer'
    private final byte[] mReadBuffer = new byte[512];
    private final ByteBuffer mWriteBuffer = ByteBuffer.allocate(BUF_SIZE);
    private ResponseDataCallback mListener;
    private IState mState = IState.STOPPED;
    private final SerialInterface mSerialUtil;
    private boolean DEBUG;

    private int sleepTimeEmpty = 1;

    /**
     * 读到数据为null时，减小cpu压力，默认休眠1毫秒
     *
     * @param sleepTimeEmpty 休眠时间（毫秒）
     */
    public void setSleepTimeEmpty(int sleepTimeEmpty) {
        this.sleepTimeEmpty = sleepTimeEmpty;
    }

    public SerialIoManager(SerialInterface serial) {
        this(serial, false);
    }

    public SerialIoManager(SerialInterface serial, boolean isDebug) {
        super(TAG);
        DEBUG = isDebug;
        mSerialUtil = serial;
    }

    /**
     * 异步写入数据
     *
     * @param data 数据
     */
    public void syncWrite(byte[] data) {
        if (data == null) return;
        synchronized (mWriteBuffer) {
            while (mWriteBuffer.capacity() - mWriteBuffer.position() < data.length) {
                //fix: 2018-02-28 解决数据越界问题
                if (DEBUG) Log.d(TAG, "syncWrite: wait...");
                try {
                    mWriteBuffer.wait(100);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
            mWriteBuffer.put(data);
        }
    }

    /**
     * 同步写入数据
     *
     * @param data 数据
     */
    public synchronized void write(byte[] data) {
        try {
            mSerialUtil.write(data);
            mListener.sendData(data);
        } catch (IOException e) {
            mListener.onRunError(e);
        }
    }

    public void setListener(ResponseDataCallback listener) {
        mListener = listener;
    }

    public void stopMe() {
        if (mState == IState.STOPPED) {
            Log.i(TAG, "Already Stopped");
            return;
        }
        if (mState == IState.RUNNING) {
            Log.i(TAG, "Stop requested");
            mState = IState.STOPPING;
        }
    }

    public IState getStated() {
        return mState;
    }

    @Override
    public void run() {
        synchronized (this) {
            if (getStated() != IState.STOPPED) {
                throw new IllegalStateException("Already running.");
            }
            mState = IState.RUNNING;
        }
        Log.i(TAG, "Running ..");
        try {
            while (true) {
                if (getStated() != IState.RUNNING) {
                    Log.i(TAG, "Stopping mState=" + getState());
                    break;
                }
                nextStep();
            }
        } catch (Exception e) {
            Log.w(TAG, "Run ending due to exception: " + e.getMessage(), e);
            if (mListener != null) {
                mListener.onRunError(e);
            }
        } finally {
            synchronized (this) {
                mState = IState.STOPPED;
                Log.i(TAG, "Stopped.");
            }
        }
    }

    private void nextStep() throws Exception {
        //读数据
        //数据解析
        int len = mSerialUtil.read(mReadBuffer);
        if (len > 0) {
            if (DEBUG) Log.d(TAG, "Read data len=" + len);
            if (mListener != null) mListener.responseData(mReadBuffer, len);
        }
        //写数据
        //Handle outgoing data.
        byte[] outBuff = null;
        synchronized (mWriteBuffer) {
            len = mWriteBuffer.position();
            if (len > 0) {
                outBuff = new byte[len];
                mWriteBuffer.rewind();
                mWriteBuffer.get(outBuff, 0, len);
                mWriteBuffer.clear();
            }
        }
        if (outBuff != null) {
            // 写数据
            if (DEBUG) Log.d(TAG, "Write data len=" + len);
            mSerialUtil.write(outBuff, 0, outBuff.length);
            if (mListener != null) mListener.sendData(outBuff);
        } else {
            Thread.sleep(sleepTimeEmpty);
        }
    }

    public enum IState {
        STOPPED, RUNNING, STOPPING
    }

    public interface ResponseDataCallback {
        void responseData(byte[] data, int len);

        void sendData(byte[] data);

        void onRunError(Exception e);
    }
}
