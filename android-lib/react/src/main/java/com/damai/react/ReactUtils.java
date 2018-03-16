package com.damai.react;

import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renxueliang on 16/10/14.
 */
public class ReactUtils {


    public static WritableMap toWriteMap(Map<String, Object> data){

        WritableMap map = Arguments.createMap();
        for(Map.Entry<String,Object> entry : data.entrySet()){
            String key = entry.getKey();
            Object obj = entry.getValue();
            if(obj instanceof String) {
                map.putString(key, (String)obj);
            } else if(obj instanceof Integer) {
                map.putInt(key, ((Integer)obj).intValue());
            } else if(obj instanceof Double) {
                map.putDouble(key, ((Double)obj).doubleValue());
            } else if(obj instanceof Integer) {
                map.putInt(key, ((Integer)obj).intValue());
            } else if(obj instanceof Boolean) {
                map.putBoolean(key, ((Boolean)obj).booleanValue());
            }
        }
        return map;
    }

    public static WritableMap toWriteMap(JSONObject jsonObject) throws JSONException {
        WritableMap map = Arguments.createMap();
        JSONArray arr = jsonObject.names();
        if(arr==null){
            return map;
        }
        for(int i=0 , c = arr.length() ; i < c; ++i){
            String key = arr.getString(i);
            if(jsonObject.isNull(key)){
                map.putNull(key);
                continue;
            }
            Object obj = jsonObject.get(key);
            if(obj instanceof  String){
                map.putString(key,(String)obj);
            }else if(obj instanceof Integer){
                map.putInt(key,(Integer) obj);
            }else if(obj instanceof  Double){
                map.putDouble(key,(Double)obj);
            }else if(obj instanceof  Integer){
                map.putInt(key,(Integer)obj);
            }else if(obj instanceof  JSONObject){
                map.putMap(key,toWriteMap((JSONObject)obj));
            }else if(obj instanceof  JSONArray){
                map.putArray(key,toWriteArray((JSONArray)obj));
            }else if(obj instanceof  Boolean){
                map.putBoolean(key,(Boolean) obj);
            }
        }
        return map;
    }


    public static WritableArray toWriteArray(JSONArray arr) throws JSONException {
        WritableArray result = Arguments.createArray();
        for(int i=0 , c = arr.length() ; i < c; ++i){
            Object value = arr.get(i);
            if(value instanceof  JSONObject){
                JSONObject jsonObject = arr.getJSONObject(i);
                WritableMap map= ReactUtils.toWriteMap(jsonObject);
                result.pushMap(map);
            }else if(value instanceof String){

                result.pushString((String)value);
            }else if(value instanceof Integer){
                result.pushInt((Integer)value);
            }else if(value instanceof Double){
                result.pushDouble((Double)value);
            }

        }
        return result;
    }
    public static Bundle toBundle(ReadableMap data){
        //文件
        Bundle result = new Bundle();
        ReadableMapKeySetIterator it = data.keySetIterator();

        while(it.hasNextKey()){
            String key = it.nextKey();
            ReadableType readType = data.getType(key);
            switch (readType){
                case Null:
                   // result.putString(key,null);
                    break;
                case Boolean:
                    result.putBoolean(key,data.getBoolean(key));
                    break;
                case Number:
                {
                    double d = data.getDouble(key);
                    double cd = Math.floor(d);
                    if(d!=cd){
                        result.putDouble(key,d);
                    }else{
                        result.putInt(key,(int)d);
                    }

                }
                break;
                case String:
                    result.putString(key,data.getString(key));
                    break;
                case Map:
                {
                    ReadableMap readableMap = data.getMap(key);
                    result.putBundle(key, toBundle(readableMap));
                }
                break;
                case Array:
                {
                    throw new RuntimeException("Not support yet!");
                   // ReadableArray arr = data.getArray(key);
                   // result.put(key,toArray(arr));
                }
            }
        }
        return result;
    }

    public static Map<String,Object> toMap(ReadableMap data){
        //文件
        Map<String,Object> result = new HashMap<String,Object>();
        ReadableMapKeySetIterator it = data.keySetIterator();

        while(it.hasNextKey()){
            String key = it.nextKey();
            ReadableType readType = data.getType(key);
            switch (readType){
                case Null:
                    result.put(key,null);
                    break;
                case Boolean:
                    result.put(key,data.getBoolean(key));
                    break;
                case Number:
                {
                    double d = data.getDouble(key);
                    double cd = Math.floor(d);
                    if(d!=cd){
                        result.put(key,d);
                    }else{
                        result.put(key,(int)d);
                    }

                }
                    break;
                case String:
                    result.put(key,data.getString(key));
                    break;
                case Map:
                {
                    ReadableMap readableMap = data.getMap(key);
                    result.put(key, toMap(readableMap));
                }
                    break;
                case Array:
                {
                    ReadableArray arr = data.getArray(key);
                    result.put(key,toArray(arr));
                }
                    break;
            }
        }
        return result;
    }

    public static List toArray(ReadableArray arr){
        int c = arr.size();
        List result = new ArrayList(c);
        for(int i=0; i < c ;++i){
            ReadableType readType = arr.getType(i);
            switch (readType){
                case Null:
                    result.add(null);
                    break;
                case Boolean:
                    result.add(arr.getBoolean(i));
                    break;
                case Number:
                {
                    double d = arr.getDouble(i);
                    double cd = Math.floor(d);
                    if(d!=cd){
                        result.add((int)d);
                    }else{
                        result.add(d);
                    }

                }
                break;
                case String:
                    result.add(arr.getString(i));
                    break;
                case Map:
                {
                    ReadableMap readableMap = arr.getMap(i);

                    result.add( toMap(readableMap));
                }
                break;
                case Array:
                {
                    ReadableArray ar = arr.getArray(i);
                    result.add(toArray(ar));
                }
                break;
            }
        }
        return  result;
    }




    public static JSONObject toJSONObject(ReadableMap data) throws JSONException {
        //文件
        JSONObject result = new JSONObject();
        ReadableMapKeySetIterator it = data.keySetIterator();

        while(it.hasNextKey()){
            String key = it.nextKey();
            ReadableType readType = data.getType(key);
            switch (readType){
                case Null:
                    result.put(key,null);
                    break;
                case Boolean:
                    result.put(key,data.getBoolean(key));
                    break;
                case Number:
                {
                    double d = data.getDouble(key);
                    double cd = Math.floor(d);
                    if(d!=cd){
                        result.put(key,d);
                    }else{
                        result.put(key,(int)d);
                    }

                }
                break;
                case String:
                    result.put(key,data.getString(key));
                    break;
                case Map:
                {
                    ReadableMap readableMap = data.getMap(key);
                    result.put(key, toJSONObject(readableMap));
                }
                break;
                case Array:
                {
                    ReadableArray arr = data.getArray(key);
                    result.put(key,toJSONArray(arr));
                }
                break;
            }
        }
        return result;
    }

    public static JSONArray toJSONArray(ReadableArray arr) throws JSONException {
        int c = arr.size();
        JSONArray result = new JSONArray();
        for(int i=0; i < c ;++i){
            ReadableType readType = arr.getType(i);
            switch (readType){
                case Null:
                    result.put(null);
                    break;
                case Boolean:
                    result.put(arr.getBoolean(i));
                    break;
                case Number:
                {
                    double d = arr.getDouble(i);
                    double cd = Math.floor(d);
                    if(d!=cd){
                        result.put((int)d);
                    }else{
                        result.put(d);
                    }

                }
                break;
                case String:
                    result.put(arr.getString(i));
                    break;
                case Map:
                {
                    ReadableMap readableMap = arr.getMap(i);

                    result.put( toMap(readableMap));
                }
                break;
                case Array:
                {
                    ReadableArray ar = arr.getArray(i);
                    result.put(toArray(ar));
                }
                break;
            }
        }
        return  result;
    }
}
