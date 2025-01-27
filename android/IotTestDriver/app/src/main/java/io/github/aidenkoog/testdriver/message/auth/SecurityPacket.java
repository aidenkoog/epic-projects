package io.github.aidenkoog.testdriver.message.auth;

import android.util.Log;

import io.github.aidenkoog.testdriver.message.exception.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

public class SecurityPacket implements Serializable {

    private static final String TAG = SecurityPacket.class.getSimpleName();

    private static final byte CRYPTO_A128CBC    = 0x40;
    private static final byte CRYPTO_SHA256     = 0x10;
    private static final byte CRYPTO_A128KW     = 0x01;

    public static final byte[] CRYPTO_HS_AAD   = { CRYPTO_A128CBC|CRYPTO_SHA256|CRYPTO_A128KW };

    private byte[] header;
    private byte[] cek;
    private byte[] iv;
    private byte[] encrypt;
    private byte[] signature;

    private static byte[] random16() {
        //  Get random short(2bytes) value
        byte[] id16 = new byte[16];

        Random rand = new Random();
        rand.nextBytes(id16);

        return id16;
    }

    private static byte[] random32() {
        //  Get random int(4bytes) value
        byte[] id32 = new byte[32];

        Random rand = new Random();
        rand.nextBytes(id32);

        return id32;
    }

    public byte[] getHeader() {
        return header;
    }

    public int getHeaderLen() {
        return header.length;
    }

    public byte[] getCek() {
        return cek;
    }

    public int getCekLen() {
        return cek.length;
    }

    public byte[] getIV() {
        return iv;
    }

    public int getIVLen() {
        return iv.length;
    }

    public byte[] getEncrypt() {
        return encrypt;
    }

    public int getEncryptLen() {
        return encrypt.length;
    }

    public byte[] getSignature() {
        return signature;
    }

    public int getSignatureLen() {
        return signature.length;
    }

    private int getPacketLen() {
        return getHeaderLen() + getCekLen() + getIVLen() + getEncryptLen() + getSignatureLen() + 8;
    }

    public byte[] getDecryptBytes() {

        byte[] upperRn32 = new byte[16];
        byte[] lowerRn32 = new byte[16];

        byte[] rn32 = KeyWrapper.unwrapping(cek);

        System.arraycopy(rn32, 0, upperRn32, 0, 16);
        System.arraycopy(rn32, 16, lowerRn32, 0, 16);

        if (HashMac.verifySignature(encrypt, upperRn32, iv, signature) == true) {
            return Encryptor.decrypt(encrypt, lowerRn32, iv);
        }

        return null;
    }

    public SecurityPacket(final byte[] content) {

        byte[] upperRn32 = new byte[16];
        byte[] lowerRn32 = new byte[16];

        byte[] rn32 = random32();

        System.arraycopy(rn32, 0, upperRn32, 0, 16);
        System.arraycopy(rn32, 16, lowerRn32, 0, 16);

        header = CRYPTO_HS_AAD;

        iv = random16();
        cek = KeyWrapper.wrapping(rn32);

        encrypt = Encryptor.encrypt(content, lowerRn32, iv);
        signature = HashMac.getHashMac(encrypt, upperRn32, iv);
    }

    public SecurityPacket(final List<Byte> packet) throws PacketInvalidException {

        int cekLen, ivLen, encryptedLen, signatureLen;

        try {
            if (packet == null || packet.isEmpty() == true) {
                throw new PacketInvalidException("Encryption data is wrong");
            }

            header = new byte[] { (byte)packet.remove(0) };
            if (Arrays.equals(header, CRYPTO_HS_AAD) == false) {
                throw new PacketInvalidException("Imposibble to be decrypted");
            }

            cekLen = (short)(packet.remove(0) << 8 | packet.remove(0) << 0);
            if (cekLen != 40) {
                throw new PacketInvalidException("Wrong Dialog Key");
            }

            cek = new byte[cekLen];
            for (int i = 0; i < cekLen; i++) {
                cek[i] = packet.remove(0);
            }

            ivLen = (short)(packet.remove(0) << 8 | packet.remove(0) << 0);
            if (ivLen != 16) {
                throw new PacketInvalidException("Wrong Key");
            }

            iv = new byte[ivLen];
            for (int i = 0; i < ivLen; i++) {
                iv[i] = packet.remove(0);
            }

            encryptedLen = (short)(packet.remove(0) << 8 | packet.remove(0) << 0);
            encrypt = new byte[encryptedLen];
            for (int i = 0; i < encryptedLen; i++) {
                encrypt[i] = packet.remove(0);
            }

            signatureLen = (short)(packet.remove(0) << 8 | packet.remove(0) << 0);
            if (signatureLen != 16) {
                throw new PacketInvalidException("Wrong Hash Mac");
            }

            signature = new byte[signatureLen];
            for (int i = 0; i < signatureLen; i++) {
                signature[i] = packet.remove(0);
            }

        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
            throw new PacketInvalidException("ConcurrentModificationException");
        }
    }

    public static int getValidSecurityPacketLen(final List<Byte> packet)
                            throws PacketLackException, PacketInvalidException {

        int i = 0;
        short dialogKeyLen, keyLen, encryptLen, hashMacLen;

        if (packet == null || packet.isEmpty() == true) {
            throw new PacketInvalidException("Encryption data is wrong");
        }

        if (packet.get(i++) != CRYPTO_HS_AAD[0]) {
            throw new PacketInvalidException("Imposibble to be decrypted");
        }

        dialogKeyLen = (short)(packet.get(i++) << 8 | packet.get(i++) << 0);
        if (dialogKeyLen != 40) {
            throw new PacketInvalidException("Wrong Dialog Key");
        }
        if ((packet.size() - i) < dialogKeyLen) {
            throw new PacketLackException("Lack of Dialog Key");
        }

        i += dialogKeyLen;
        keyLen = (short)(packet.get(i++) << 8 | packet.get(i++) << 0);
        if (keyLen != 16) {
            throw new PacketInvalidException("Wrong Key");
        }
        if ((packet.size() - i) < keyLen) {
            throw new PacketLackException("Lack of Key");
        }

        i += keyLen;
        encryptLen = (short)(packet.get(i++) << 8 | packet.get(i++) << 0);
        if ((packet.size() - i) < encryptLen) {
            throw new PacketLackException("Lack of Encrypt");
        }

        i += encryptLen;
        hashMacLen = (short)(packet.get(i++) << 8 | packet.get(i++) << 0);
        if (hashMacLen != 16) {
            throw new PacketInvalidException("Wrong Hash Mac");
        }
        if ((packet.size() - i) < hashMacLen) {
            throw new PacketLackException("Lack of Hash Mac");
        }
        i += hashMacLen;

        return i;
    }

    public byte[] getBytes() {

        int i = 0;
        byte[] bytes = new byte[getPacketLen()];

        for (byte b : getHeader()) {
            bytes[i++] = b;
        }

        bytes[i++] = (byte)((getCekLen() & 0xFF00) >> 8);
        bytes[i++] = (byte)((getCekLen() & 0x00FF) >> 0);

        for (byte b : getCek()) {
            bytes[i++] = b;
        }

        bytes[i++] = (byte)((getIVLen() & 0xFF00) >> 8);
        bytes[i++] = (byte)((getIVLen() & 0x00FF) >> 0);

        for (byte b : getIV()) {
            bytes[i++] = b;
        }

        bytes[i++] = (byte)((getEncryptLen() & 0xFF00) >> 8);
        bytes[i++] = (byte)((getEncryptLen() & 0x00FF) >> 0);

        for (byte b : getEncrypt()) {
            bytes[i++] = b;
        }

        bytes[i++] = (byte)((getSignatureLen() & 0xFF00) >> 8);
        bytes[i++] = (byte)((getSignatureLen() & 0x00FF) >> 0);

        for (byte b : getSignature()) {
            bytes[i++] = b;
        }

        return bytes;
    }
}

