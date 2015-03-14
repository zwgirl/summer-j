package org.bark.remoting;

import javax.json.JsonObjectBuilder;

public class IntSerializer implements Serializer{
	@Override
	public void writeObject(JsonObjectBuilder builder, ReferenceProcessor handler,
			Object value) {
		builder.add(LarkConstants.CLASS, value.getClass().getName());
		builder.add(LarkConstants.VALUE, (int)value);
		
	}
}