package org.bark.remoting;

import javax.json.JsonObjectBuilder;

public class BooleanSerializer implements Serializer {

	@Override
	public void writeObject(JsonObjectBuilder builder, ReferenceProcessor handler,
			Object value) {
		builder.add(LarkConstants.CLASS, LarkConstants.JAVA_LANG_BOOLEAN);
		builder.add(LarkConstants.VALUE, (boolean) value);
	}

}
