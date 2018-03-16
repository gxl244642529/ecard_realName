package com.damai.core;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用来解析json数据
 * @author Randy
 *
 */
public interface ApiParser {

    /**
     * 将服务器json，根据class转化为具体的实体类或列表等
     * @param json
     * @param clazz
     * @return
     * @throws JSONException
     */
    Object parse(JSONObject json,Class<?> clazz) throws JSONException;

}
