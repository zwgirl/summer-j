package org.bark.remoting;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;


public class ArrayListSerializer implements Serializer {

	 @Override
     public void writeObject(JsonObjectBuilder builder, ReferenceProcessor handler, Object value) {
//       org.bark.remoting.SerializerFactory.getInstance().getSerializer(ArrayList.class).writeObject(builder, handler, value);
       ArrayList list = (ArrayList)value;
       final JsonBuilderFactory bf = Json.createBuilderFactory(null);
	   JsonArrayBuilder elements = bf.createArrayBuilder();
	   
       for(Object obj : list){
    	   elements.add(handler.shared(obj));
       }
       builder.add("value", elements);
     }
}
