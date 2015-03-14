package org.bark.remoting;

import javax.json.JsonObjectBuilder;

public class DoubleSerializer implements Serializer {

	@Override
	public void writeObject(JsonObjectBuilder builder, ReferenceProcessor handler,
			Object value) {
		builder.add(LarkConstants.CLASS, LarkConstants.JAVA_LANG_DOUBLE);
		builder.add(LarkConstants.VALUE, (double) value);
	}
}