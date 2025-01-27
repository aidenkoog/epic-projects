package io.github.aidenkoog.testdriver.message.auth;

import static io.github.aidenkoog.testdriver.message.auth.SecurityPacket.CRYPTO_HS_AAD;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HashMac {

    private static final String TAG = HashMac.class.getSimpleName();

    private static final String TRANSFORMATION = "HmacSHA256";

    private static final String ALGORITHM = "AES";

    private static final byte[] CRYPTO_HS_AL = { (byte)0x00,
                                                 (byte)0x00,
                                                 (byte)0x00,
                                                 (byte)0x00,
                                                 (byte)0x00,
                                                 (byte)0x00,
                                                 (byte)0x00,
                                                 (byte)0x08
                                               };

    private static byte[] getHashMacInput(final byte[] encrypt, final byte[] iv) {

        byte[] hMacInput = null;
        List<Byte> listByte = new ArrayList<Byte>();

        for (byte b : CRYPTO_HS_AAD) {
            listByte.add(b);
        }

        for (byte b : iv) {
            listByte.add(b);
        }

        for (byte b : encrypt) {
            listByte.add(b);
        }

        for (byte b : CRYPTO_HS_AL) {
            listByte.add(b);
        }

        int i = 0; hMacInput = new byte[listByte.size()];
        for (Byte b : listByte) {
            hMacInput[i++] = b.byteValue();
        }

        return hMacInput;
    }

    public static byte[] getHashMac(final byte[] encrypt, final byte[] key, final byte[] iv) {

        byte[] hashMac = null;
        byte[] input = getHashMacInput(encrypt, iv);

        try {
            byte[] digest = null;

            Mac hasher = Mac.getInstance(TRANSFORMATION);
            hasher.init(new SecretKeySpec(key, ALGORITHM));

            digest = hasher.doFinal(input);
            hashMac = new byte[16];

            System.arraycopy(digest, 0, hashMac, 0, 16);

        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        return hashMac;
    }

    public static boolean verifySignature(final byte[] encrypt,
                final byte[] key, final byte[] iv, final byte[] signature) {

        byte[] hashMac = getHashMac(encrypt, key, iv);

        if (Arrays.equals(hashMac, signature) == true) {
            return true;
        }

        return false;
    }
}






















