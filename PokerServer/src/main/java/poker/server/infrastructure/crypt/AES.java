package poker.server.infrastructure.crypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	private static String CIPHER_ALGORITHM = "AES";
	private static String CIPHER_TRANSFORMATION = "SHA1PRNG";

	private final static String HEX = "0123456789ABCDEF";

	public static String encrypt(String seed, String cleartext)
			throws Exception {

		byte[] RAWKey = getRawKey(seed.getBytes());
		System.out.println("Raw : " +RAWKey.toString());
		byte[] ciphertext = encrypt(RAWKey, cleartext.getBytes());
		return toHex(ciphertext);
	}

	public static String decrypt(String seed, String encrypted) {

		byte[] RAWKey = null;
		try {
			RAWKey = getRawKey(seed.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		byte[] encryption = toByte(encrypted);
		byte[] plaintext = null;
		try {
			plaintext = decrypt(RAWKey, encryption);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(plaintext);
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {

		KeyGenerator keygen = KeyGenerator.getInstance(CIPHER_ALGORITHM);
		SecureRandom sr = SecureRandom.getInstance(CIPHER_TRANSFORMATION);
		sr.setSeed(seed);
		keygen.init(128, sr);
		SecretKey skey = keygen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {

		SecretKeySpec skeySpec = new SecretKeySpec(raw, CIPHER_ALGORITHM);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted) {

		SecretKeySpec skeySpec = new SecretKeySpec(raw, CIPHER_ALGORITHM);
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (NoSuchPaddingException e) {

			e.printStackTrace();
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		} catch (InvalidKeyException e) {

			e.printStackTrace();
		}

		byte[] decrypted = null;

		try {
			decrypted = cipher.doFinal(encrypted);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return decrypted;
	}

	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {

		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	public static String toHex(byte[] buf) {

		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

}