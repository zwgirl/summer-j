package org.bark.remoting;

import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;

public class HashMapSerializer implements Serializer {

	//{__class : "java.util.HashMap", value=[[kRef1,vRef1], [kRef2, vRef2]...]
	@SuppressWarnings("unchecked")
	@Override
    public void writeObject(JsonObjectBuilder builder, ReferenceProcessor handler, Object value) {
        Map map = (Map) value;
        final JsonBuilderFactory bf = Json.createBuilderFactory(null);
	    JsonArrayBuilder elements = bf.createArrayBuilder();
	    
	    map.forEach((Object key, Object v)->{
	    	JsonArrayBuilder entry = bf.createArrayBuilder();
	    	entry.add(handler.shared(key));
	    	entry.add(handler.shared(v));
	    	elements.add(entry);
	    });
        
        builder.add(LarkConstants.VALUE, elements);
	}
}
