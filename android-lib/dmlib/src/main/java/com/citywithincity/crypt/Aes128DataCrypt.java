package com.citywithincity.crypt;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;


public class Aes128DataCrypt implements IDataCrypt{
	private static final String AESTYPE ="AES/ECB/PKCS5Padding"; 
	Cipher enCipher;
	Cipher deCipher;
	@SuppressLint("TrulyRandom")
	public void setKey(byte[] keystr) throws Exception{
		 Key key = new SecretKeySpec(keystr, "AES"); 
		 enCipher = Cipher.getInstance(AESTYPE); 
		 enCipher.init(Cipher.ENCRYPT_MODE, key);
		 
		 deCipher = Cipher.getInstance(AESTYPE); 
		 deCipher.init(Cipher.DECRYPT_MODE, key);
	}
	
	@Override
	public byte[] encript(byte[] data, byte[] key) throws Exception{
		setKey(key);
        return enCipher.doFinal(data);
	}

	@Override
	public byte[] decript(byte[] data, byte[] key) throws Exception {
		setKey(key);
		return deCipher.doFinal(data);
	}

}
