package org.bark.remoting;

import static org.bark.remoting.LarkConstants.CLASS;
import static org.bark.remoting.LarkConstants.VALUE;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

public class LarkOutput {
	public JsonArray writeObject(Object obj){
	    final JsonBuilderFactory bf = Json.createBuilderFactory(null);
	    JsonArrayBuilder root = bf.createArrayBuilder();
	    JsonObjectBuilder firstElement = bf.createObjectBuilder();
	    
	    if(obj == null){
	    	firstElement.add(CLASS, Object.class.getName());
	    	firstElement.addNull(VALUE);
	    	root.add(firstElement);
	    } else {
	    	Class<?> clazz = obj.getClass();
	    	Serializer ser = SerializerFactory.getInstance().getSerializer(clazz);
	    	Handler handler = new Handler();
	    	ser.writeObject(firstElement, handler, obj);
	    	root.add(firstElement);
	    	int current = 1;
	    	while(current < handler.getShared().length){
	    		Object shared = handler.getShared()[current++];
	    		JsonObjectBuilder otherElement = bf.createObjectBuilder();
	    		root.add(otherElement);
		    	ser = SerializerFactory.getInstance().getSerializer(shared.getClass());
		    	ser.writeObject(otherElement, handler, shared);
	    	}
	    }
	   
		return root.build();
	}
	
	public static void main(String[] args) {
		LarkOutput l = new LarkOutput();

		OutputStream out = new ByteArrayOutputStream();
		
		 JsonWriter writer = Json.createWriter(out);
	     System.out.println(out);
		
	}
}
