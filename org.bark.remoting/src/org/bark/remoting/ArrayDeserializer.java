package org.bark.remoting;

import java.lang.reflect.Array;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class ArrayDeserializer implements Deserializer {

	public Object readObject(JsonObject jsonObj, Object[] handlers) {
		try {
			String className = jsonObj.getString(LarkConstants.CLASS);
			JsonArray array = jsonObj.getJsonArray(LarkConstants.VALUE);
			Class<?> clazz = Class.forName(className);
			int length = array.size();
			Object[] result = (Object[]) Array.newInstance(clazz.getComponentType(), length);
			for (int i = 0; i < length; i++) {
				result[i] = handlers[array.getInt(i)];
			}
			return result;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
