package android.serialport;

import android.serialport.utils.SerialInterface;
import android.util.Log;

import java.nio.ByteBuffer;


/**
 * @author Created by Administrator on  2017/12/1
 * @version 1.0.
 */

public class SerialIoManager extends Thread {

    private static final int BUFSIZ = 4096;
    // Synchronized by 'mWriteBuffer'
    private final ByteBuffer mWriteBuffer = ByteBuffer.allocate(BUFSIZ);
    private ResponseDataCallback mListener;
    private State mState = State.STOPPED;
    private final SerialInterface mSerialUtil;
    private boolean DEBUG = true;

    public SerialIoManager(SerialInterface serial) {
        mSerialUtil = serial;
    }

    public void syncWrite(byte[] data) {
        if (data == null) return;
        synchronized (mWriteBuffer) {
            while (mWriteBuffer.capacity() - mWriteBuffer.position() < data.length) {
                //fix: 2018-02-28 解决数据越界问题
                try {
                    mWriteBuffer.wait(100);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
            mWriteBuffer.put(data);
        }
    }

    public synchronized void setListener(ResponseDataCallback listener) {
        mListener = listener;
    }

    public synchronized void stopMe() {
        if (getStated() == State.RUNNING) {
            Log.i(TAG, "Stop requested");
            mState = State.STOPPING;
        }
    }

    private synchronized State getStated() {
        return mState;
    }

    private static final String TAG = "SerialIoManager";

    @Override
    public void run() {
        synchronized (this) {
            if (getStated() != State.STOPPED) {
                throw new IllegalStateException("Already running.");
            }
            mState = State.RUNNING;
        }
        Log.i(TAG, "Running ..");
        try {
            while (true) {
                if (getStated() != State.RUNNING) {
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
                mState = State.STOPPED;
                Log.i(TAG, "Stopped.");
            }
        }
    }

    private void nextStep() throws Exception {
        //读数据
        //数据解析
        int len = 0;
        byte[] dataByte = mSerialUtil.getDataByte();
        if (dataByte != null && (len = dataByte.length) > 0) {
            if (DEBUG) Log.d(TAG, "Read data len=" + len);
            if (mListener != null)
                mListener.responseData(dataByte);
        } else Thread.sleep(200);
        //写数据
        // Handle outgoing data.
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
            mSerialUtil.setData(outBuff, 0, outBuff.length);
            if (mListener != null) mListener.sendData(outBuff);
        }
    }

    private enum State {
        STOPPED, RUNNING, STOPPING
    }

    public interface ResponseDataCallback {
        void responseData(byte[] data);

        void sendData(byte[] data);

        void onRunError(Exception e);
    }
}
