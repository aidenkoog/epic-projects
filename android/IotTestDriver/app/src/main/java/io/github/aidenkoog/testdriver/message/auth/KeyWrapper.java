package io.github.aidenkoog.testdriver.message.auth;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class KeyWrapper {

    private static final String TAG = KeyWrapper.class.getSimpleName();

    private static final String TRANSFORMATION = "AES/ECB/NoPadding";

    private static final String ALGORITHM = "AES";

    private static final byte[] KEY = { (byte)0x36,
                                        (byte)0x8B,
                                        (byte)0x01,
                                        (byte)0x39,
                                        (byte)0x6D,
                                        (byte)0x89,
                                        (byte)0x1F,
                                        (byte)0x66,
                                        (byte)0x40,
                                        (byte)0xD5,
                                        (byte)0x98,
                                        (byte)0xD2,
                                        (byte)0x40,
                                        (byte)0x54,
                                        (byte)0x4C,
                                        (byte)0xB8
                                      };

    private static final byte[] IV  = { (byte)0xA6,
                                        (byte)0xA6,
                                        (byte)0xA6,
                                        (byte)0xA6,
                                        (byte)0xA6,
                                        (byte)0xA6,
                                        (byte)0xA6,
                                        (byte)0xA6
                                      };

    public static byte[] wrapping(final byte[] key32) {

        byte[] dialogueKey = new byte[40];

        try {
            byte[] in = new byte[16];
            byte[] out = new byte[16];

            SecretKeySpec keySpec = new SecretKeySpec(KEY, "AES");

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            System.arraycopy(key32, 0, dialogueKey, 0, 32);
            System.arraycopy(IV, 0, out, 0, 8);

            for (int j = 0; j < 6; j++) {
                for (int i = 0; i < (key32.length / 8); i++) {
                    System.arraycopy(out, 0, in, 0, 8);
                    System.arraycopy(dialogueKey, i*8, in, 8, 8);

                    out = cipher.doFinal(in);


                    System.arraycopy(out, 8, dialogueKey, i*8, 8);

                    out[7] ^= ((key32.length / 8)*j) + i + 1;
                }
            }

            System.arraycopy(dialogueKey, 0, dialogueKey, 8, 32);
            System.arraycopy(out, 0, dialogueKey, 0, 8);
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

        return dialogueKey;
    }

    public static byte[] unwrapping(final byte[] dialogKey) {

        byte[] key = new byte[32];

        try {
            byte[] in = new byte[16];
            byte[] out = new byte[16];

            SecretKeySpec keySpec = new SecretKeySpec(KEY, "AES");

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            System.arraycopy(dialogKey, 8, key, 0, 32);
            System.arraycopy(dialogKey, 0, out, 0, 8);

            for (int j = 5; j >= 0 ; j--) {
                for (int i = (key.length / 8) - 1; i >= 0; i--) {

                    out[7] ^= ((key.length / 8) * j) + i + 1;

                    System.arraycopy(out, 0, in, 0, 8);
                    System.arraycopy(key, i*8, in, 8, 8);

                    out = cipher.doFinal(in);

                    System.arraycopy(out, 8, key, i*8, 8);
                }
            }

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

        return key;
    }
}
