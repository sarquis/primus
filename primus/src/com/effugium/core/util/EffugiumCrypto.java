package com.effugium.core.util;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EffugiumCrypto {
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = new byte[] { 'A', 'c', 'j', ']', '=', 'z', 'b', 'K', '{', '~', 'v', '9', '-',
	    '&', '/', '0', 'e', 'f', 'f', 'u', 'g', 'i', 'u', 'm' };

    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data is a string
     * @return the encrypted string
     */
    public static String encrypt(String data) throws Exception {
	Key key = generateKey();
	Cipher c = Cipher.getInstance(ALGORITHM);
	c.init(Cipher.ENCRYPT_MODE, key);
	byte[] encVal = c.doFinal(data.getBytes());
	return Base64.getEncoder().encodeToString(encVal);
    }

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData is a string
     * @return the decrypted string
     */
    public static String decrypt(String encryptedData) throws Exception {
	Key key = generateKey();
	Cipher c = Cipher.getInstance(ALGORITHM);
	c.init(Cipher.DECRYPT_MODE, key);
	byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
	byte[] decValue = c.doFinal(decordedValue);
	return new String(decValue);
    }

    /**
     * Generate a new encryption key.
     */
    private static Key generateKey() throws Exception {
	return new SecretKeySpec(keyValue, ALGORITHM);
    }
}
