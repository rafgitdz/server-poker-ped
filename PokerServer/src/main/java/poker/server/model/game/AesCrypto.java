package poker.server.model.game;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
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

	public byte[] encrypt(String input) {

		byte[] encrypted = null;

		try {

			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encrypted = cipher.doFinal(input.getBytes());

		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return decrypted;
	}

	public static String bytesToString(byte[] input) {
		String output = Hex.encodeHexString(input);
		return output;
	}
	
	public static byte[] stringToBytes(String input) {
		
		char[] inputChar = input.toCharArray();
		byte[] output = null;
		
		try {
			output = Hex.decodeHex(inputChar);
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output;
	}
}
