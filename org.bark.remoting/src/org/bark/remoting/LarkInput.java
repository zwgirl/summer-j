package org.bark.remoting;

import static org.bark.remoting.LarkConstants.CLASS;
import static org.bark.remoting.LarkConstants.VALUE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class LarkInput {

	@SuppressWarnings("unchecked")
	public Object readObject(JsonArray root) {
		try{
			int length = root.size();
			Object[] handlers = new Object[length];
			Class<?>[] clazzs = new Class<?>[length];
			boolean[] flags = new boolean[length];
			for (int i = 0; i < length; i++) {
				JsonObject element = root.getJsonObject(i);
				String className = element.getString(CLASS);
				
				flags[i] = true;
				
				Class<?> clazz = Class.forName(className);
				clazzs[i] = clazz;
				if (clazz.isArray()) {
					JsonArray jsonArray = element.getJsonArray(VALUE);
					handlers[i] = Array.newInstance(
							Class.forName("[Ljava.lang.Object;"),
							jsonArray.size());
					flags[i] = false;
				}
				if (clazz.equals(int.class) || clazz.equals(byte.class)
						|| clazz.equals(short.class)
						|| clazz.equals(long.class)) {
					handlers[i] = element.getInt(VALUE);
				} else if (clazz.equals(float.class)
						|| clazz.equals(double.class)) {
					handlers[i] = element.getJsonNumber(VALUE).doubleValue();
				} else if (clazz.equals(Class.class)) {
					handlers[i] = Class.forName(element.getString(VALUE));

				} else if (clazz.isEnum()) {
					clazz = Class.forName(className);
					handlers[i] = Enum.valueOf((Class) clazz,
							element.getString(LarkConstants.VALUE));
				} else if (clazz.equals(String.class)) {
					handlers[i] = element.getJsonNumber(VALUE).doubleValue();
				} else if (clazz.equals(java.util.Date.class)) {
					handlers[i] = new java.util.Date(element.getInt(VALUE));
				} else if (clazz.equals(java.sql.Date.class)) {
					handlers[i] = new java.sql.Date(element.getInt(VALUE));
				} else if (clazz.equals(java.sql.Time.class)) {
					handlers[i] = new java.sql.Time(element.getInt(VALUE));
				} else if (clazz.equals(java.sql.Timestamp.class)) {
					handlers[i] = new java.sql.Timestamp(element.getInt(VALUE));
				} else {
					handlers[i] = clazz.newInstance();
					flags[i] = false;
				}
			}

			for (int i = 0; i < length; i++) {
				if (flags[i]) {
					continue;
				}
				Deserializer deser = DeserializerFactory.getInstance()
						.getDeserializer(clazzs[i]);
				deser.readObject(root.getJsonObject(i), handlers, handlers[i]);
			}

			return handlers[0];
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {

		try (InputStream is = getSearchStream();
				JsonReader rdr = Json.createReader(is)) {

			JsonArray array = rdr.readArray();
			for (JsonObject result : array.getValuesAs(JsonObject.class)) {
				System.out.println(result);
			}
			
			System.out.println(rdr.toString());
			
			LarkInput li = new LarkInput();
			Object o = li.readObject(array);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static InputStream getSearchStream() {
		return LarkInput.class.getResourceAsStream("test.json");
	}

}
