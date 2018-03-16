package com.citywithincity.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.util.Base64;
 

/**
 * AES加密类，用于和php通信
 * @author Randy
 *
 */
public class CryptAES {
	private static final String AESTYPE ="AES/ECB/PKCS5Padding"; 
	 
	/**
	 * 加密
	 * @param keyStr		加密密钥，24位
	 * @param plainText		加密文本
	 * @return
	 */
    @SuppressLint("TrulyRandom")
	public static String encrypt(String keyStr, String plainText) { 
        byte[] encrypt = null; 
        try{ 
            Key key = generateKey(keyStr); 
            Cipher cipher = Cipher.getInstance(AESTYPE); 
            cipher.init(Cipher.ENCRYPT_MODE, key); 
            encrypt = cipher.doFinal(plainText.getBytes());     
        }catch(Exception e){ 
            e.printStackTrace(); 
        }
        return new String(Base64.encode(encrypt,Base64.NO_WRAP)); 
    } 
 
    /**
     * 解密
     * @param keyStr		解密密钥(24位)
     * @param encryptData	密文
     * @return
     */
    public static String decrypt(String keyStr, String encryptData) {
        byte[] decrypt = null; 
        try{ 
            Key key = generateKey(keyStr); 
            Cipher cipher = Cipher.getInstance(AESTYPE); 
            cipher.init(Cipher.DECRYPT_MODE, key); 
            decrypt = cipher.doFinal(Base64.decode(encryptData,0)); 
        }catch(Exception e){ 
            e.printStackTrace(); 
        } 
        return new String(decrypt).trim(); 
    } 
 
    private static Key generateKey(String key)throws Exception{ 
        try{            
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES"); 
            return keySpec; 
        }catch(Exception e){ 
            e.printStackTrace(); 
            throw e; 
        } 
 
    } 
 
   
}
