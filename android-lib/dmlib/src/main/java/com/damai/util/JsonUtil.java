package com.damai.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Base64;

import com.citywithincity.utils.Alert;
import com.citywithincity.utils.IoUtil;
@SuppressLint("DefaultLocale")
public class JsonUtil {
	
	public static String toJson(Map<?, ?> map){
		StringBuilder sb = new StringBuilder();
		mapToJson(sb, map);
		return sb.toString();
	}	
	
	public static void mapToJson(StringBuilder sb, Map<?,?> map) {
		boolean first = true;
		sb.append(MAP_START);
		for (Entry<?, ?> entry : map.entrySet()) {
			if (first)
				first = false;
			else
				sb.append(',');
			toKeyValue(String.valueOf(entry.getKey()), entry.getValue(), sb);
		}
		sb.append(MAP_END);
	}

	
	
	protected static void toKeyValue(String key, Object value, StringBuilder sb) {
		sb.append('\"');
		if (key == null)
			sb.append("null");
		else
			escape(key, sb);
		sb.append('\"').append(':');
		toJson(sb, value);
	}
	
	private static final char ARRAY_START = '[';
	private static final char ARRAY_END = ']';
	private static final char MAP_START = '{';
	private static final char MAP_END = '}';
	
	protected static void listToJson(StringBuilder sb, List<?> list) {
		boolean first = true;
		sb.append(ARRAY_START);
		for (Object object : list) {
			if (first)
				first = false;
			else
				sb.append(',');
			toJson(sb, object);
		}
		sb.append(ARRAY_END);
	}
	
	protected static void arrayToJson(StringBuilder sb,Object[] array) {
		boolean first = true;
		sb.append(ARRAY_START);
		for (Object object : array) {
			if (first)
				first = false;
			else
				sb.append(',');
			toJson(sb, object);
		}
		sb.append(ARRAY_END);
	}

	/**
	 * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters
	 * (U+0000 through U+001F).
	 */
	@SuppressLint("DefaultLocale")
	protected static void escape(String s, StringBuilder sb) {
		if (s == null) {
			sb.append("null");
			return;
		}
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			// case '/':
			// sb.append("\\/");
			// break;
			default:
				if ((ch >= '\u0000' && ch <= '\u001F')
						|| (ch >= '\u007F' && ch <= '\u009F')
						|| (ch >= '\u2000' && ch <= '\u20FF')) {
					String str = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - str.length(); k++) {
						sb.append('0');
					}
					sb.append(str.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	protected static void toJson(StringBuilder sb, Object value) {
		if (value == null) {
			sb.append("null");
		} else if (value instanceof String) {
			sb.append("\"");
			escape((String) value, sb);
			sb.append("\"");
		} else if (value instanceof Double) {
			if (((Double) value).isInfinite() || ((Double) value).isNaN())
				sb.append("null");
			else
				sb.append(value.toString());
		} else if (value instanceof Float) {
			if (((Float) value).isInfinite() || ((Float) value).isNaN())
				sb.append("null");
			else
				sb.append(value.toString());
		} else if (value instanceof Number) {
			sb.append(value.toString());
		} else if (value instanceof Boolean) {
			sb.append(value.toString());
		} else if (value instanceof Map) {
			mapToJson(sb, (Map) value);
		} else if (value instanceof List) {
			listToJson(sb, (List) value);
		} else if (value instanceof Object[]) {
			arrayToJson(sb, (Object[])value);
		} else if (value instanceof Enum) {
			sb.append("\"");
			sb.append(((Enum) value).toString());
			sb.append("\"");
		} else if (value instanceof Character) {
			sb.append("\"");
			escape(value.toString(), sb);
			sb.append("\"");
		}else if(value instanceof File) {
            sb.append("\"");
            try {
				String base64 = Base64.encodeToString(IoUtil.readBytes(  (File)value  ),Base64.NO_WRAP);
                sb.append(URLEncoder.encode(base64,"UTF-8"));
            } catch (IOException e) {
                Alert.showShortToast("图片解析失败");
            }
            sb.append("\"");
		}else if(value instanceof byte[]){
            sb.append("\"");
			String base64 = Base64.encodeToString( (byte[])value,Base64.NO_WRAP);
			try {
				sb.append(URLEncoder.encode(base64,"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sb.append("\"");
		}else if(value instanceof JSONArray){
            sb.append( ((JSONArray)value).toString() );
		}else if(value instanceof  JSONObject){
            sb.append( ((JSONObject)value).toString() );
		}else{
			beanToJson(sb, value);
		}
	}

	protected static void beanToJson(StringBuilder sb, Object model) {
		sb.append(MAP_START);
		boolean first = false;
		Class<?> clazz = model.getClass();
		/**
		 * 这里只使用public field
		 */
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			try{
				Object value = field.get(model);
				if(first){
					first = false;
				}else{
					sb.append(',');
				}
				toJson(sb, value);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//getter
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			String name = method.getName();
			if( (method.getParameterTypes().length ==0 && name.startsWith("is") && name.length() > 2 ) || (name.startsWith("get") && name.length() > 3)){
				try {
					Object value = method.invoke(model);
					if(first){
						first = false;
					}else{
						sb.append(',');
					}
					toJson(sb, value);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		sb.append(MAP_END);
	}
}
