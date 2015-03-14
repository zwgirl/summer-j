package org.bark.remoting;

import javax.json.JsonObject;

public class IntDeserializer implements Deserializer{


	@Override
	public Object readObject(JsonObject jsonObj, Object[] handlers, Object obj) {
		return jsonObj.getJsonNumber(LarkConstants.VALUE).doubleValue();
	}

}
