package org.bark.remoting;

import javax.json.JsonObject;

public class BooleanDeserializer implements Deserializer{

	@Override
	public Object readObject(JsonObject jsonObj, Handler handler) {
		return jsonObj.getBoolean(LarkConstants.VALUE);
	}

}
