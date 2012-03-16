package poker.server.infrastructure.crypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AesCrypto {

	private Cipher cipher;
	private KeyGenerator kgen;
	private SecretKeySpec skeySpec;

	public AesCrypto() {

		try {

			kgen = KeyGenerator.getInstance("AES");
			kgen.init(128); // 192 and 256 bits may not be available
			SecretKey skey = kgen.generateKey();
			byte[] raw = skey.getEncoded();

			skeySpec = new SecretKeySpec(raw, "AES");

			cipher = Cipher.getInstance("AES");

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}

	}

	public Cipher getCipher() {
		return cipher;
	}

	public KeyGenerator getKgen() {
		return kgen;
	}

	public SecretKeySpec getSkeySpec() {
		return skeySpec;
	}

	/**
	 * Turns array of bytes into string
	 * 
	 * @param buf
	 *            Array of bytes to convert to hex string
	 * @return Generated hex string
	 * 
	 */
	public String asHex(byte buf[]) {

		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;

		for (i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10)
				strbuf.append("0");

			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
		}

		return strbuf.toString();
	}

	public byte[] encrypt(String input) {

		byte[] encrypted = null;

		try {

			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encrypted = cipher.doFinal(input.getBytes());

		} catch (InvalidKeyException e) {

		} catch (IllegalBlockSizeException e) {

			e.printStackTrace();
		} catch (BadPaddingException e) {

			e.printStackTrace();
		}

		return encrypted;
	}

	public String decrypt(byte[] input) {

		String decrypted = null;

		try {

			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] original = cipher.doFinal(input);
			decrypted = new String(original);

		} catch (InvalidKeyException e) {

			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {

			e.printStackTrace();
		} catch (BadPaddingException e) {

			e.printStackTrace();
		}

		return decrypted;
	}
}
