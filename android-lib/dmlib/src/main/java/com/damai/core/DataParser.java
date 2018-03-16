package com.damai.core;

import com.damai.dmlib.DMParseException;

public interface DataParser<T extends HttpJobImpl> {
    /**
     * 解析数据
     * @param data
     */
    Object parseData(T api,byte[] data) throws DMParseException;
}
