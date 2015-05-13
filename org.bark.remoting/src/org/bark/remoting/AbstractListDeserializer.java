package org.bark.remoting;

import javax.json.JsonObject;

public class AbstractListDeserializer implements Deserializer {

	@Override
	public Object readObject(JsonObject jsonObj, Object[] handlers, Object obj) throws Exception {
		return obj;
	}

}
