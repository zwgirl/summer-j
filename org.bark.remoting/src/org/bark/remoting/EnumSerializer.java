package org.bark.remoting;

import javax.json.JsonObjectBuilder;

public class EnumSerializer implements Serializer {

	@Override
	public void writeObject(JsonObjectBuilder builder, Handler handler,
			Object value) {
		builder.add(LarkConstants.CLASS, value.getClass().getName());
		builder.add(LarkConstants.VALUE, ((Enum<?>)value).name());
	}

}
