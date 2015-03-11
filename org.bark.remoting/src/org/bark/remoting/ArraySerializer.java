package org.bark.remoting;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;

public class ArraySerializer implements Serializer {

	public ArraySerializer() {
	}

	@Override
	public void writeObject(JsonObjectBuilder builder, Handler handler,
			Object value) {
		builder.add(LarkConstants.CLASS, value.getClass().getName());
		final JsonBuilderFactory bf = Json.createBuilderFactory(null);
		JsonArrayBuilder array = bf.createArrayBuilder();
		builder.add(LarkConstants.VALUE, array);
		for (Object obj : (Object[]) value) {
			int refId = handler.sharedHandler(obj);
			array.add(refId);
		}
	}
}
