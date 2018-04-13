/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.nativec.tools;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class SerialPort {

    private static final String TAG = "SerialPort";
    private static final String vName = "1.0.2";

    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    //添加文件锁，如果文件锁不能获取，则说明此文件被其它文件占用，则需释放锁，然后重新开启（例如进程间广播释放文件锁）
    private RandomAccessFile mAccessFile;
    private FileChannel mChannel;
    private FileLock mLock;

    public SerialPort(File device, int baudrate, int flags) throws Exception {
        /* Check access permission */
        if (!device.canRead() || !device.canWrite()) {
            try {
                Process su;
                su = Runtime.getRuntime().exec("su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
                        + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                throw new SecurityException();
            }
        }
        //获取文件锁
        lock(device);
        //开启
        mFd = open(device.getAbsolutePath(), baudrate, flags);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    public void lock(File file) throws Exception {
        release();
        //获取文件锁
        mAccessFile = new RandomAccessFile(file.getAbsolutePath(), "rw");
        mChannel = mAccessFile.getChannel();
        int i = 5;
        try {
            do {
                i--;
                mLock = mChannel.tryLock();
            } while (mLock == null && i > 0);
        } catch (Exception e) {
            release();
            throw new LockException("file lock is null", e);
        }
        if (mLock == null) {
            release();
            throw new LockException("file lock is null");
        }
    }

    public void release() {
        //获取文件锁
        try {
            if (mLock != null) {
                mLock.release();
                mLock = null;
            }
            if (mChannel != null) {
                mChannel.close();
                mChannel = null;
            }
            if (mAccessFile != null) {
                mAccessFile.close();
                mAccessFile = null;
            }
        } catch (Exception e) {
            //
        }
    }


    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    // JNI
    private native static FileDescriptor open(String path, int baudrate, int flags);

    public native void close();

    public native void tcflush();

    static {
        Log.d(TAG, "static initializer: " + vName);
        System.loadLibrary("serial_port");
    }
}
