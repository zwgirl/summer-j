package org.bark.remoting;

import javax.json.JsonObjectBuilder;

public interface Serializer {
	void writeObject(JsonObjectBuilder builder, Handler handler, Object value);
}
