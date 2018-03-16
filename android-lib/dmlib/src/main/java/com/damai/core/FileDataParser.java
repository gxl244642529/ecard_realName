package com.damai.core;

import java.io.File;
import java.io.IOException;

import com.citywithincity.utils.IoUtil;
import com.damai.dmlib.DMParseException;

public class FileDataParser implements DataParser<HttpJobImpl> {

	@Override
	public Object parseData(HttpJobImpl api, byte[] data)
			throws DMParseException {
		
		File file = api.getData();
		//这里写入文件
		try {
			IoUtil.writeBytes(file, data);
		} catch (IOException e) {
			throw new DMParseException("文件写入失败");
		}
		
		return file;
	}

}
