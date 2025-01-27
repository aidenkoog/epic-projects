package io.github.aidenkoog.testdriver.system;

import android.content.Context;
import android.os.Build;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.TelephonyManager;
import android.util.Log;

import io.github.aidenkoog.testdriver.BleIot;

import java.util.ArrayList;
import java.util.List;

public class CellTowerManager {

    private static final String TAG = CellTowerManager.class.getSimpleName();

    TelephonyManager mTelephonyManager;

    private CellTowerManager() {
        mTelephonyManager = (TelephonyManager) BleIot.getContext().getSystemService(Context.TELEPHONY_SERVICE);
    }

    private static class SingletonData {
        private static final CellTowerManager instance = new CellTowerManager();
    }

    public static CellTowerManager getInstance() {
        return SingletonData.instance;
    }

    public static class CellInfoEx {
        private String type;
        private int mcc;
        private int mnc;
        private int lac;
        private int cellId;
        private int strength;

        CellInfoEx(String type, int mcc, int mnc, int lac, int cellId, int strength) {
            this.type = type;
            this.mcc = mcc;
            this.mnc = mnc;
            this.lac = lac;
            this.cellId = cellId;
            this.strength = strength;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getMcc() {
            return mcc;
        }

        public void setMcc(int mcc) {
            this.mcc = mcc;
        }

        public int getMnc() {
            return mnc;
        }

        public void setMnc(int mnc) {
            this.mnc = mnc;
        }

        public int getLac() {
            return lac;
        }

        public void setLac(int lac) {
            this.lac = lac;
        }

        public int getCellId() {
            return cellId;
        }

        public void setCellId(int cellId) {
            this.cellId = cellId;
        }

        public int getStrength() {
            return strength;
        }

        public void setStrength(int strength) {
            this.strength = strength;
        }

        public String toString() {
            return String.format("%d,%d,%d,%x,%d", mcc, mnc, lac, cellId, strength);
        }
    }

    public List<CellInfoEx> getCellTowerInfo() {
        int mcc, mnc, lac, cellId, strength;
        List<CellInfoEx> cellInfoEx = new ArrayList<CellInfoEx>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<CellInfo> infos = mTelephonyManager.getAllCellInfo();

            for (int i = 0; i < infos.size(); i++) {
                try {
                    CellInfo info = infos.get(i);

                    if (info instanceof CellInfoGsm) {
                        CellSignalStrengthGsm strengthGsm = ((CellInfoGsm) info).getCellSignalStrength();
                        CellIdentityGsm identity = ((CellInfoGsm) info).getCellIdentity();

                        mcc = identity.getMcc();
                        mnc = identity.getMnc();
                        lac = identity.getLac();
                        cellId = identity.getCid();
                        strength = strengthGsm.getDbm();

                        if (mcc == 0 || mnc == 0 || lac == 0 || cellId == 0) break;

                        cellInfoEx.add(new CellInfoEx("gsm", mcc, mnc, lac, cellId, strength));
                    } else if (info instanceof CellInfoLte) {
                        CellSignalStrengthLte strengthLte = ((CellInfoLte) info).getCellSignalStrength();
                        CellIdentityLte identity = ((CellInfoLte) info).getCellIdentity();

                        mcc = identity.getMcc();
                        mnc = identity.getMnc();
                        lac = identity.getTac();
                        cellId = identity.getCi();
                        strength = strengthLte.getDbm();

                        Log.e(TAG, "mcc:" + mcc);
                        Log.e(TAG, "mnc:" + mnc);
                        Log.e(TAG, "lac:" + lac);
                        Log.e(TAG, "cellId:" + cellId);
                        Log.e(TAG, "strength:" + strength);

                        if (mcc > 1000 || mcc == 0 || mnc == 0 || lac == 0 || cellId == 0) break;

                        cellInfoEx.add(new CellInfoEx("lte", mcc, mnc, lac, cellId, strength));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return cellInfoEx;
    }
}
