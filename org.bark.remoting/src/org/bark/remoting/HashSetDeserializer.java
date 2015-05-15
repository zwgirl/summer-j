package org.bark.remoting;

import java.util.HashSet;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class HashSetDeserializer implements Deserializer {

	//{__class : "java.util.HashMap", value=[[kRef1,vRef1], [kRef2, vRef2]...]
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public Object readObject(JsonObject json, Object[] handlers, Object obj) throws Exception {
        HashSet set = (HashSet)obj;
        JsonArray elements = (JsonArray) json.get(LarkConstants.VALUE);
        if(elements != null){
        	for(int i=0,size=elements.size();i<size;i++){
        		set.add(handlers[elements.getInt(i)]);
        	}
        }
        return set;
	}
}
