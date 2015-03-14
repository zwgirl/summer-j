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
	    JsonArrayBuilder array = bf.createArrayBuilder();
	    if(obj == null){
		    JsonObjectBuilder first = bf.createObjectBuilder();
	    	first.add(CLASS, Object.class.getName());
	    	first.addNull(VALUE);
	    	array.add(first);
	    } else {
	    	ReferenceProcessor referenceProcess = new ReferenceProcessor();
	    	referenceProcess.shared(obj);
	    	
	    	int current = 0;
	    	while(current < referenceProcess.getShares().length){
	    		JsonObjectBuilder element = bf.createObjectBuilder();
	    		Object shared = referenceProcess.getShares()[current++];
	    		
	    		element.add(CLASS, obj.getClass().getName());
	    		
	    		Serializer ser = SerializerFactory.getInstance().getSerializer(shared.getClass());
		    	ser.writeObject(element, referenceProcess, shared);
	    		array.add(element);
		    	System.out.println(element);
	    	}
	    }
	   
		return array.build();
	}
	
	public static void main(String[] args) {
		Person parent = new Person("sasa", 10, null);
		Person p = new Person("sasa", 10, parent);
		
		LarkOutput l = new LarkOutput();
		JsonArray ja = l.writeObject(p);

		OutputStream out = new ByteArrayOutputStream();
		
		 JsonWriter writer = Json.createWriter(out);
		 writer.writeArray(ja);
	     System.out.println(out);
		
	}
}



