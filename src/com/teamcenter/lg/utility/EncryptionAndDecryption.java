package com.teamcenter.lg.utility;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionAndDecryption {
	private String key="KEO2951lzQAsT6dn4DlM1g==";
	private SecretKey secKey;
	private int T_LEN = 128;
	private byte[] IV;
	
	private byte[] decode(String data) {
		return Base64.getDecoder().decode(data); 
	}
	private String encode(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}
	public EncryptionAndDecryption() {
		IV=decode("Yw4x0065TUdTHPhr");
		secKey=new SecretKeySpec(decode(key), "AES");
	}
	public String encrypt(String data) throws Exception {
		byte[] dataInBytes = data.getBytes();
		Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding"); 
		GCMParameterSpec spec= new GCMParameterSpec (T_LEN, IV);
		encryptionCipher.init(Cipher.ENCRYPT_MODE, secKey, spec); byte[] encryptedBytes = encryptionCipher.doFinal (dataInBytes);
		return encode (encryptedBytes);

	}
	public String decrypt (String encryptedMessage) throws Exception {
		byte[] messageInBytes = decode (encryptedMessage);
		Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding"); 
		GCMParameterSpec spec= new GCMParameterSpec (T_LEN, IV);
		decryptionCipher.init(Cipher. DECRYPT_MODE, secKey, spec);
		byte[] decryptedBytes = decryptionCipher.doFinal (messageInBytes);
		return new String(decryptedBytes);
	}
}
