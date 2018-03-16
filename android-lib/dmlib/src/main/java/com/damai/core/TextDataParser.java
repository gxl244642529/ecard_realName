package com.damai.core;

import com.damai.dmlib.DMParseException;

@SuppressWarnings("rawtypes")
public class TextDataParser implements DataParser {

	/**
	 * 解析成text
	 */
	@Override
	public Object parseData(HttpJobImpl api, byte[] data) throws DMParseException {
		return new String(data);
	}

}
