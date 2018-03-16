package com.citywithincity.crypt;

public interface IDataCrypt {
	/**
	 * Encript data with key to encripted base64 text
	 * @param data
	 * @param key
	 * @return
	 */
	byte[] encript(byte[] data,byte[] key) throws Exception;
	
	/**
	 * Decript base64 string with key to decripted text
	 * @param data
	 * @param key
	 * @return
	 */
	byte[] decript(byte[] data,byte[] key) throws Exception;
}
