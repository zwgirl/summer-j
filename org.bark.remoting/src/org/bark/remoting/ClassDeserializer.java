package org.bark.remoting;

import javax.json.JsonObject;

public class ClassDeserializer implements Deserializer {

	@Override
	public Object readObject(JsonObject jsonObj, Handler handler) {
		try {
			String className = jsonObj.getString(LarkConstants.CLASS);
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
