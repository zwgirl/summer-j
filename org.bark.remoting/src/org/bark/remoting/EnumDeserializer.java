package org.bark.remoting;

import javax.json.JsonObject;

public class EnumDeserializer implements Deserializer {

	@Override
	public Object readObject(JsonObject jsonObj, Handler handler) {
		try {
			String className = jsonObj.getString(LarkConstants.CLASS);
			Class<?> clazz = Class.forName(className);
			return Enum.valueOf((Class) clazz,
					jsonObj.getString(LarkConstants.VALUE));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
