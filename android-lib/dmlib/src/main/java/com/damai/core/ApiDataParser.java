package com.damai.core;

import org.json.JSONException;
import org.json.JSONObject;

import com.damai.dmlib.DMParseException;

public class ApiDataParser implements DataParser<ApiJobImpl> {

    private ApiParser[] parsers;

    public ApiDataParser(ApiParser[] parsers){
        this.parsers = parsers;
    }

    @Override
    public Object parseData(ApiJobImpl api,byte[] data) throws DMParseException {
        try {
            JSONObject jsonObject = new JSONObject(new String(data));
            return parsers[api.server].parse(jsonObject, api.entity);
        } catch (JSONException e) {
            throw new DMParseException(e);
        }
    }

}
