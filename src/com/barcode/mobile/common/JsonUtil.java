package com.barcode.mobile.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 序列化对象为JSON格式 遵循JSON组织公布标准
 * 
 * @date 2008/05/07
 * @version 1.0.0
 */
public class JsonUtil {

	// /**
	// * 将字符串转成对象
	// *
	// * @param json json字符串
	// * @param cla 转换成BEAN对应的CLASS
	// * @return
	// */
	// @SuppressWarnings("unchecked")
	// public static <T> T json2Bean(String json, Class<T> cla) {
	// JSONObject jsonObject = JSONObject.fromObject(json);
	// return (T) JSONObject.toBean(jsonObject, cla);
	// }
	//    
	// /**
	// * 将字符串转成对象list
	// *
	// * @param <E>
	// * @param jsonStr
	// * @param cla
	// * @return
	// */
	// @SuppressWarnings("unchecked")
	// public static <E> List<E> json2List(String jsonStr, Class<E> cla) {
	// List<E> itemList = new ArrayList<E>();
	// JSONArray jsonarray = JSONArray.fromObject(jsonStr);
	// for (int i = 0; i < jsonarray.size(); i++) {
	// JSONObject object = jsonarray.getJSONObject(i);
	// E item = (E)JSONObject.toBean(object, cla);
	// itemList.add(item);
	// }
	// return itemList;
	// }

	/**
	 * 将json字符串转换为指定Class
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <E> E Json2Bean(final String json, Class<E> clazz)
			throws Exception {
		Object object = null;
		JSONObject jsonObject = null;
		if (!StringUtil.isBlank(json)) {
			try {
				jsonObject = new JSONObject(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			object = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				setJson2Field(object, jsonObject, field);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (isNULL(jsonObject, object)) {
			throw new Exception("转换对象错误，不能转换" + object.getClass());
		}
		return (E) object;
	}

	@SuppressWarnings("unchecked")
	private static boolean isNULL(JSONObject jsonObject, Object clazz) {
		if (clazz == null || jsonObject == null) {
			return true;
		}
		Iterator<String> iterator = jsonObject.keys();
		List<String> s = new ArrayList<String>();
		do {
			s.add(iterator.next());
		} while (iterator.hasNext());
		int num = 0;
		try {
			Field[] fields = clazz.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				String name = fields[i].getName();
				if (s.contains(name)) {
					num++;
				}
			}
		} catch (Exception e) {

		}
		if (num == s.size())
			return false;
		return true;
	}

	private static void setField(Object object, JSONObject jsonObject,
			Field field) throws Exception {
		String name = field.getName();
		String type = field.getType().getSimpleName();
		Object value = jsonObject.get(name);
		if (value instanceof String) // String
		{
			field.set(object, jsonObject.getString(name));
		} else if (value instanceof Boolean) // Boolean
		{
			field.set(object, jsonObject.getBoolean(name));
		} else if (value instanceof Number) // Number
		{
			if (value instanceof Integer) {
				field.set(object, jsonObject.getInt(name));
			} else if (value instanceof Float) {
				field.set(object, Float.parseFloat(value.toString()));
			} else if (value instanceof Double) {
				field.set(object, Float.parseFloat(value.toString()));
			} else if (value instanceof Long) {
				field.set(object, jsonObject.getLong(name));
			} else if (value instanceof Short) {
				field.set(object, Short.parseShort(value.toString()));
			}
		} else if (value instanceof JSONArray) {
			if (int[].class.getSimpleName().equals(type)) {
				JSONArray values = jsonObject.getJSONArray(name);
				int[] ss = new int[values.length()];
				for (int i = 0; i < values.length(); i++) {
					ss[i] = values.getInt(i);
				}
				field.set(object, ss);
			} else if (double[].class.getSimpleName().equals(type)) {
				JSONArray values = jsonObject.getJSONArray(name);
				double[] ss = new double[values.length()];
				for (int i = 0; i < values.length(); i++) {
					ss[i] = values.getDouble(i);
				}
				field.set(object, ss);
			} else if (float[].class.getSimpleName().equals(type)) {
				JSONArray values = jsonObject.getJSONArray(name);
				float[] ss = new float[values.length()];
				for (int i = 0; i < values.length(); i++) {
					ss[i] = Float.parseFloat(values.get(i).toString());
				}
				field.set(object, ss);
			} else if (long[].class.getSimpleName().equals(type)) {
				JSONArray values = jsonObject.getJSONArray(name);
				long[] ss = new long[values.length()];
				for (int i = 0; i < values.length(); i++) {
					ss[i] = values.getLong(i);
				}
				field.set(object, ss);
			} else if (String[].class.getSimpleName().equals(type)) {
				JSONArray values = jsonObject.getJSONArray(name);
				String[] ss = new String[values.length()];
				for (int i = 0; i < values.length(); i++) {
					ss[i] = values.getString(i);
				}
				field.set(object, ss);
			} else if (Object[].class.getSimpleName().equals(type)) {
				JSONArray values = jsonObject.getJSONArray(name);
				Object[] ss = new Object[values.length()];
				for (int i = 0; i < values.length(); i++) {
					ss[i] = Json2Bean(values.getJSONArray(i).toString(), values
							.get(i).getClass());
				}
				field.set(object, ss);
			} else if (List.class.getSimpleName().equals(type)
					|| ArrayList.class.getSimpleName().equals(type)) {
				JSONArray values = jsonObject.getJSONArray(name);
				ArrayList list = new ArrayList(values.length());
				for (int i = 0; i < values.length(); i++) {
					list.add(values.get(i));
				}
				field.set(object, list);
			}
		} else if (value instanceof JSONObject) // 保底收尾对象
		{
			if (Map.class.getSimpleName().equals(type)) {
				JSONObject obj = jsonObject.getJSONObject(name);
				Map<String, Object> map = new HashMap<String, Object>();
				Iterator<String> keys = obj.keys();
				do {
					String s = keys.next();
					map.put(s, obj.get(s));
				} while (keys.hasNext());
				field.set(object, map);
			} else {
				try {
					JSONObject jo = jsonObject.getJSONObject(name);
					Object obj = Json2Bean(jo.toString(), Class.forName(field
							.getType().getName()));
					field.set(object, obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void setJson2Field(Object object, JSONObject jsonObject,
			Field field) {
		field.setAccessible(true);
		try {
			setField(object, jsonObject, field);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}