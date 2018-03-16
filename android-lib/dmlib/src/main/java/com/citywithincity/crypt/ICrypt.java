package com.citywithincity.crypt;

/**
 * 用于加密 
 * @author Randy
 *
 */
public interface ICrypt {
	String encrypt(String data,String key) throws Exception;
	String decrypt(String data,String key) throws Exception;
	
}
