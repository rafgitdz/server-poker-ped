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

/**
 * This involves some tools to encrypt and decrypt request strings, using AES
 * algorithm.
 * <p>
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 */
public class AES {

	private static String CIPHER_ALGORITHM = "AES";
	private static String CIPHER_TRANSFORMATION = "SHA1PRNG";

	private final static String HEX = "0123456789ABCDEF";

	/**
	 * Encrypt a clear text using AES algorithm.
	 * 
	 * @param seed
	 *            The password given by the consumer to generate the encryption
	 *            key. this password must be correct to start the encryption.
	 * 
	 * @param cleartext
	 *            The string to encrypt.
	 * 
	 * @return The encrypted text, in hex format.
	 * 
	 * @throws Exception
	 *             if the encrypting go wrong.
	 */
	public static String encrypt(String seed, String cleartext)
			throws Exception {

		byte[] RAWKey = getRawKey(seed.getBytes());
		byte[] ciphertext = encrypt(RAWKey, cleartext.getBytes());
		return toHex(ciphertext);
	}

	/**
	 * decrypt an encrypted text using AES algorithm.
	 * 
	 * @param seed
	 *            The password given by the consumer to generate the encryption
	 *            key. this password must be correct to reverse the encryption.
	 * 
	 * @param encrypted
	 *            The encrypted string to decrypt.
	 * 
	 * @return The decrypted clear text.
	 */
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

	/**
	 * Get the key, generated from a password given by the consumer. This key is
	 * required to start the encryption/decryption process.
	 * 
	 * @param seed
	 *            The password given by the consumer to generate the key.
	 * 
	 * @return Generated encryption/decryption key represented by an array of
	 *         Byte.
	 * @see Byte
	 */
	private static byte[] getRawKey(byte[] seed) throws Exception {

		KeyGenerator keygen = KeyGenerator.getInstance(CIPHER_ALGORITHM);
		SecureRandom sr = SecureRandom.getInstance(CIPHER_TRANSFORMATION);
		sr.setSeed(seed);
		keygen.init(128, sr);
		SecretKey skey = keygen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	/**
	 * Encrypt a clear text using a given encryption key
	 * 
	 * @param raw
	 *            an array of Byte, representing the encryption key.
	 * 
	 * @param clear
	 *            The string to encrypt.
	 * 
	 * @return An array of Byte representing the encrypted text, .
	 * 
	 * @throws Exception
	 *             if the encrypting go wrong.
	 * @see Byte
	 */
	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {

		SecretKeySpec skeySpec = new SecretKeySpec(raw, CIPHER_ALGORITHM);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	/**
	 * Decrypt an encrypted text using a given encryption key
	 * 
	 * @param raw
	 *            An array of Byte, representing the encryption key.
	 * 
	 * @param encrpted
	 *            An array of Byte, representing the encrypted text.
	 * 
	 * @return An array of Byte representing the decrypted text.
	 * @see Byte
	 */
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