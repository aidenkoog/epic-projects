package io.github.aidenkoog.testdriver.system;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import io.github.aidenkoog.testdriver.BleIoT;
import io.github.aidenkoog.testdriver.system.PogoArrayList;
import io.github.aidenkoog.testdriver.system.PogoList;

import static io.github.aidenkoog.testdriver.view.BleIotActivity.BEACON_SCAN_REQUEST_ENABLE_BT;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class PogoManager {

    private static final String TAG = PogoManager.class.getSimpleName();

    private static final int POGO_BEACON_COMPANY_ID = 0x4c;

    private static final byte POGO_BEACON_TYPE_ID = (byte)0x02;

    private static final byte POGO_BEACON_DATA_LEN = (byte)0x15;

    private static final byte[] POGO_BEACON_UUID = new byte[] {(byte)0x5f,
                                                               (byte)0x26,
                                                               (byte)0xd2,
                                                               (byte)0x4a,
                                                               (byte)0xc6,
                                                               (byte)0x2b,
                                                               (byte)0x11,
                                                               (byte)0xe7,
                                                               (byte)0x8c,
                                                               (byte)0x86,
                                                               (byte)0x48,
                                                               (byte)0xa7,
                                                               (byte)0xca,
                                                               (byte)0x11,
                                                               (byte)0xd2,
                                                               (byte)0xe8};

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;

    private boolean mIsScanning = false;
    private boolean mIsPogoReady = false;
    private OnPogoManagerListener mListener;
    private PogoList mPogoList = new PogoArrayList();

    private PogoManager() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
    }

    private static class SingletonData {
        private static final PogoManager instance = new PogoManager();
    }

    public static PogoManager getInstance() {
        return SingletonData.instance;
    }

    public interface OnPogoManagerListener {
        void onPogoReady();
    }

    public boolean isPogoReady() {
        return mIsPogoReady;
    }

    public int getPogoCount() {
        Log.e(TAG, "pogo size:" + mPogoList.size());
        return mPogoList.size();
    }

    public String getPogoAddress() {
        if (mPogoList.size() == 0) {
            return null;
        }
        //  sort
        Collections.sort(mPogoList);
        for (Pogo pogo : mPogoList) {
            Log.e(TAG, "sort:" + pogo);
        }
        for (Pogo pogo : mPogoList) {
            if (pogo.isExpired() == false) {
                Log.e(TAG, "added:" + pogo);
                return pogo.getAddress();
            }
        }
        return null;
    }

    public boolean enableBluetooth() {
        Context context = BleIoT.getContext();

        if (mBluetoothAdapter == null || !context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            // Device does not support Bluetooth Low Energy
            Log.e(TAG, "Bluetooth Low Energy not supported.");
            Toast.makeText(context, "Bluetooth Low Energy is not supported on this device", Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                if (!mBluetoothAdapter.enable()) {
                    Intent enableBtIntent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    ((Activity)context).startActivityForResult(enableBtIntent, BEACON_SCAN_REQUEST_ENABLE_BT);
                    return false;
                }
            }
        }
        return true;
    }

    public void startScan() {
        mPogoList.clear();

        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (mBluetoothLeScanner == null) {
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }

        if (mIsScanning == true || enableBluetooth() == false) {
            return;
        }
        mIsScanning = true;

        ScanSettings scanSettings = new ScanSettings.Builder()
                                                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                                                    .build();

        byte[] manufacturerData = new byte[POGO_BEACON_UUID.length + 2];
        manufacturerData[0] = POGO_BEACON_TYPE_ID;
        manufacturerData[1] = POGO_BEACON_DATA_LEN;
        System.arraycopy(POGO_BEACON_UUID, 0, manufacturerData, 2, POGO_BEACON_UUID.length);

        ScanFilter scanFilter = new ScanFilter.Builder()
                                              .setManufacturerData(POGO_BEACON_COMPANY_ID, manufacturerData)
                                              .build();

        List<ScanFilter> scanFilters = new Vector<>();
        scanFilters.add(scanFilter);

        mBluetoothLeScanner.startScan(scanFilters, scanSettings, mScanCallback);
        mIsPogoReady = false;
    }

    public void startScan(final OnPogoManagerListener listener) {
        mListener = listener;

        startScan();
    }

    public void stopScan() {
        mIsScanning = false;
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (mBluetoothLeScanner == null) {
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }

        mBluetoothLeScanner.stopScan(mScanCallback);

        mPogoList.clear();
        mIsPogoReady = false;
    }

    ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
                if (mIsScanning == false) {
                    return;
                }
                if (mIsPogoReady == false) {
                    mIsPogoReady = true;
                    if (mListener != null) {
                        mListener.onPogoReady();
                    }
                }
                mPogoList.addPogo(new Pogo(result.getDevice().getAddress(), result.getRssi()));
        }
    };
 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == BEACON_SCAN_REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                startScan();
            }
        }
    }
}
