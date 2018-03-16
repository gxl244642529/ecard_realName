package com.citywithincity.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.citywithincity.models.FileObject;

public class FileDatabase {

	@SuppressWarnings("unchecked")
	public static boolean jsonToObject(JSONObject json, Object result) {
		try {
			Class<?> cls = result.getClass();
			Field[] fields = cls.getFields();
			for (Field field : fields) {
				String name = field.getName();
				if (json.has(name)) {
					Object value = field.get(result);
					if (value instanceof Integer) {
						field.setInt(result, json.getInt(name));
					} else if (value instanceof Boolean) {
						field.setBoolean(result, json.getBoolean(name));
					} else if (value instanceof Float) {
						field.setFloat(result, (float) json.getDouble(name));
					} else if (value instanceof Double) {
						field.setDouble(result, json.getDouble(name));
					} else if (value instanceof List) {
						@SuppressWarnings("rawtypes")
						List list = (List) value;
						JSONArray jsonArray = json.getJSONArray(name);
						int count = jsonArray.length();
						for (int i = 0; i < count; ++i) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							String className = jsonObject.getString("class");
							Class<?> clazz = Class.forName(className);
							Object object = clazz.newInstance();
							jsonToObject(jsonObject, object);
							list.add(object);
						}
					} else if (value instanceof Long) {
						field.set(result, json.getLong(name));
					} else {
						field.set(result, json.getString(name));
					}
				}

			}
			return true;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static boolean objectToJson(Object data, JSONObject json) {
		Field[] fields = data.getClass().getFields();
		try {
			json.put("class", data.getClass().getName());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		for (Field field : fields) {
			String name = field.getName();
			Object value = null;
			try {
				value = field.get(data);
				if (value instanceof Integer) {
					json.put(name, (Integer) value);
				} else if (value instanceof Long) {
					json.put(name, (Long) value);
				} else if (value instanceof Boolean) {
					json.put(name, (Boolean) value);
				} else if (value instanceof Float) {
					json.put(name, (Float) value);
				} else if (value instanceof Double) {
					json.put(name, (float) (double) (Double) value);
				} else if (value instanceof String) {
					json.put(name, (String) value);
				} else if (value instanceof List) {
					List list = (List) value;
					JSONArray jsonArray = new JSONArray();
					for (Object object : list) {
						JSONObject jsonObject = new JSONObject();
						objectToJson(object, jsonObject);
						jsonArray.put(jsonObject);
					}
					json.put(name, jsonArray);
				} else if (value instanceof Map) {
					json.put(name, value);
				}
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	public static <T extends FileObject> boolean saveToFile(File file, T data) {
		JSONObject json = new JSONObject();
		if (objectToJson(data, json)) {
			String content = json.toString();
			try {
				IoUtil.writeToFile(content, file);
				return true;
			} catch (Exception e) {

			}

		}
		return false;
	}

	public static <T extends FileObject> T loadFromFile(File file,
			Class<T> clazz) {
		if (!file.exists())
			return null;
		String content = null;
		try {
			content = IoUtil.readFromFile(file);
		} catch (IOException e1) {
			return null;
		}
		if (TextUtils.isEmpty(content)) {
			return null;
		}
		JSONObject json;
		try {
			json = new JSONObject(content);
			T result = clazz.newInstance();
			if (jsonToObject(json, result)) {
				result.path = file.getAbsolutePath();
				result.lastModified = file.lastModified();
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private static Comparator<FileObject> ascComparator = new Comparator<FileObject>() {

		@Override
		public int compare(FileObject lhs, FileObject rhs) {
			if (lhs.createTime < rhs.createTime) {
				return -1;
			} else if (lhs.createTime == rhs.createTime) {
				return 0;
			}
			return 1;
		}
	};

	private static Comparator<FileObject> desComparator = new Comparator<FileObject>() {

		@Override
		public int compare(FileObject lhs, FileObject rhs) {
			if (lhs.createTime < rhs.createTime) {
				return 1;
			} else if (lhs.createTime == rhs.createTime) {
				return 0;
			}
			return -1;
		}
	};

	private static final FilenameFilter FILETER = new FilenameFilter() {

		@Override
		public boolean accept(File dir, String filename) {

			return false;
		}
	};

	/**
	 * 一般来说，文件的过滤器需要考虑文件名称以.json结束
	 * 
	 * @param dir
	 * @param filer
	 * @param clazz
	 * @param orderByCreateTimeAsc
	 * @return
	 */
	public static <T extends FileObject> List<T> getListFromDir(File dir,
			FilenameFilter filer, Class<T> clazz, boolean orderByCreateTimeAsc) {
		File[] files = dir.listFiles(filer);
		List<T> list = new ArrayList<T>();
		if(files==null){
			dir.mkdirs();
			files = dir.listFiles(filer);
		}
		if(files!=null){
			for (File file : files) {
				T object = loadFromFile(file, clazz);
				if (object != null) {
					list.add(object);
				}
			}
			if (orderByCreateTimeAsc) {
				Collections.sort(list, ascComparator);
			} else {
				Collections.sort(list, desComparator);
			}
		}


		return list;
	}
}
