package org.bark.remoting;

import javax.json.JsonObject;

public class DoubleDeserializer implements Deserializer{

	@Override
	public Object readObject(JsonObject jsonObj, Handler handler) {
		return jsonObj.getJsonNumber(LarkConstants.VALUE).doubleValue();
	}

}
