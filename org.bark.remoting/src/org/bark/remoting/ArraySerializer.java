package org.bark.remoting;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;

public class ArraySerializer implements Serializer {

	public ArraySerializer() {
	}

	@Override
	public void writeObject(JsonObjectBuilder builder, ReferenceProcessor handler,
			Object value) {
		if(value.getClass().getComponentType().isPrimitive()){
			throw new IllegalAccessError("not support primitive array!");
		}
		builder.add(LarkConstants.CLASS, value.getClass().getName());
		final JsonBuilderFactory bf = Json.createBuilderFactory(null);
		JsonArrayBuilder array = bf.createArrayBuilder();

		for (Object obj : (Object[]) value) {
			int refId = handler.shared(obj);
			array.add(refId);
		}
		builder.add(LarkConstants.VALUE, array);
	}
}
