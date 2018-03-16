package com.citywithincity.crypt;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.util.Base64;

@SuppressLint("TrulyRandom")
public class Aes128Crypt implements ICrypt {
	private static final String AESTYPE ="AES/ECB/PKCS5Padding"; 
	Cipher enCipher;
	Cipher deCipher;
	
	@SuppressLint("TrulyRandom")
	public Aes128Crypt() throws Exception{
		//setKey("1234567890abcdef");
	}
	
	@SuppressLint("TrulyRandom")
	public void setKey(String keystr) throws Exception{
		 Key key = new SecretKeySpec(keystr.getBytes(), "AES"); 
		 enCipher = Cipher.getInstance(AESTYPE); 
		 enCipher.init(Cipher.ENCRYPT_MODE, key);
		 
		 deCipher = Cipher.getInstance(AESTYPE); 
		 deCipher.init(Cipher.DECRYPT_MODE, key);
	}

	@SuppressLint("TrulyRandom")
	@Override
	public String encrypt(String data, String key) throws Exception {
		setKey(key);
        byte[] encrypt = enCipher.doFinal(data.getBytes());
	    return Base64.encodeToString(encrypt,Base64.NO_WRAP); 
	}

	@Override
	public String decrypt(String data, String key) throws Exception {
		setKey(key);
		byte[] encrypt = deCipher.doFinal(Base64.decode(data, Base64.NO_WRAP));
	    return new String(encrypt,"UTF-8"); 
	}

}
