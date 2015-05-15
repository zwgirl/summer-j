package org.bark.remoting;

import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;

public class HashSetSerializer implements Serializer {

	//{__class : "java.util.HashSet", value=[ref1,...refn]}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void writeObject(JsonObjectBuilder builder, ReferenceProcessor handler, Object value) {
        Set set = (Set) value;
        final JsonBuilderFactory bf = Json.createBuilderFactory(null);
	    JsonArrayBuilder elements = bf.createArrayBuilder();
	    
	    set.forEach((Object item)->{
	    	elements.add(handler.shared(item));
	    });
        
        builder.add(LarkConstants.VALUE, elements);
      }

}
