package io.github.aidenkoog.testdriver.network;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class SocketManager {

    private static final String TAG = SocketManager.class.getSimpleName();

    private static final String DIALOGUE_SERVER = "devdialogue.carescend.com";
    private static final int DIALOGUE_SERVER_PORT = 8787;
    private static final int MAX_READ_DATA_LEN = 0x400;

    private boolean mExplicitSocketClose;
    private OnSocketManagerListener mListener;
    private Socket mSocket;
    private Thread mSocketThread;
    private DataInputStream mNetworkReader;
    private DataOutputStream mNetworkWriter;

    private static class SingletonData {
        private static final SocketManager instance = new SocketManager();
    }

    public static SocketManager getInstance() {
        return SingletonData.instance;
    }

    public interface OnSocketManagerListener {
        void onSocketManagerRead(byte[] read);
        void onSocketManagerClose();
    }

    public boolean socketOpened() {
        if (mSocket != null && !mSocket.isClosed()) {
            return true;
        }
        return false;
    }

    public void initSocket() {
        try {
            mSocket = new Socket(DIALOGUE_SERVER, DIALOGUE_SERVER_PORT);

            mNetworkWriter = new DataOutputStream(mSocket.getOutputStream());
            mNetworkReader = new DataInputStream(mSocket.getInputStream());
        } catch (IOException e) {
            Log.e(TAG, "initSocket IOException : " + e.getMessage());

            mNetworkWriter = null;
            mNetworkReader = null;
        }
    }

    public void openSocket(final OnSocketManagerListener listener) {

        mListener = listener;

        if (socketOpened() == true) return;

        initSocket();

        mSocketThread = new Thread() {
            @Override
            public void run() {

                while (mSocketThread != null && !mSocketThread.isInterrupted()) {
                    try {
                        byte[] received = null;
                        byte[] data = new byte[MAX_READ_DATA_LEN];

                        int length = mNetworkReader.read(data);

                        received = new byte[length];
                        System.arraycopy(data, 0, received, 0, length);

                        mListener.onSocketManagerRead(received);
                    } catch (SocketException e) {
                        Log.e(TAG, "SocketException : " + e.getMessage());
                        if (mExplicitSocketClose == false) {
                            closeSocket();
                            mListener.onSocketManagerClose();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (mExplicitSocketClose == false) {
                            closeSocket();
                            mListener.onSocketManagerClose();
                        }
                    } catch (NullPointerException e) {
                        initSocket();
                        continue;
                    }
                }
                mExplicitSocketClose = false;
            }
        };

        mSocketThread.start();
    }

    public void closeSocket() {
        Log.e(TAG, "closeSocket");

        mExplicitSocketClose = true;

        if (mSocketThread != null) {
            mSocketThread.interrupt();
            mSocketThread = null;
        }

        try {
            if (mNetworkReader != null) {
                mNetworkReader.close();
                mNetworkReader = null;
            }

            if (mNetworkWriter != null) {
                mNetworkWriter.close();
                mNetworkWriter = null;
            }

            if (mSocket != null) {
                mSocket.close();
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean send(byte[] message) {
        boolean ret = false;
        try {
            mNetworkWriter.write(message);
            ret = true;

        } catch (IOException e) {
            e.printStackTrace();
            ret = false;
        } catch (NullPointerException e) {
            initSocket();
            ret = false;
        }
        return ret;
    }
}
