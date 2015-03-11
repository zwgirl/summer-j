package org.bark.remoting;

import javax.json.JsonObject;

public class IntDeserializer implements Deserializer{

	@Override
	public Object readObject(JsonObject jsonObj, Handler handler) {
		return jsonObj.getJsonNumber(LarkConstants.VALUE).doubleValue();
	}

}
