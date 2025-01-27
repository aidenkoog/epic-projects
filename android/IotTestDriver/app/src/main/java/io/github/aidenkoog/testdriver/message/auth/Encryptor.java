package io.github.aidenkoog.testdriver.message.auth;

import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {

    private static final String TAG = Encryptor.class.getSimpleName();

    private static final String TRANSFORMATION = "AES/CBC/NoPadding";

    private static final String ALGORITHM = "AES";

    private static byte[] padding(final byte[] body) {

        byte[] buf = null;
        int padding = 16 - (body.length % 16);
        int bufLen = ((body.length + 16) / 16 ) * 16;

        buf = new byte[bufLen];

        System.arraycopy(body, 0, buf, 0, body.length);
        for (int i = body.length; i < buf.length; i++) {
            buf[i] = (byte)padding;
        }

        return buf;
    }

    private static byte[] depadding(final byte[] body) {

        byte[] buf = null;
        int padding = body[body.length - 1];

        buf = new byte[body.length - padding];
        System.arraycopy(body, 0, buf, 0, body.length - padding);

        return buf;
    }

    public static byte[] encrypt(final byte[] message, final byte[] key, final byte[] iv) {

        byte[] encryptedMessage = null;
        byte[] paddedMessage = padding(message);

        try {

            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            encryptedMessage = cipher.doFinal(paddedMessage);

        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }

        return encryptedMessage;
    }

    public static byte[] decrypt(final byte[] message, final byte[] key, final byte[] iv) {

        byte[] decryptMessage = null;
        byte[] depaddedMessage = null;

        try {

            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            decryptMessage = cipher.doFinal(message);
            depaddedMessage = depadding(decryptMessage);

        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }

        return depaddedMessage;
    }
}
