package org.bark.remoting;

import javax.json.JsonObjectBuilder;

public class IntSerializer implements Serializer{
	@Override
	public void writeObject(JsonObjectBuilder builder, Handler handler,
			Object value) {
		builder.add(LarkConstants.CLASS, LarkConstants.JAVA_LANG_INT);
		builder.add(LarkConstants.VALUE, (int)value);
		
	}
}