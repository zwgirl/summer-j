package org.bark.remoting;

import java.util.HashMap;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class HashMapDeserializer implements Deserializer {

	//{__class : "java.util.HashMap", value=[[kRef1,vRef1], [kRef2, vRef2]...]
	@Override
    public Object readObject(JsonObject json, Object[] handlers, Object obj) throws Exception {
        HashMap map = (HashMap)obj;
        JsonArray elements = (JsonArray) json.get(LarkConstants.VALUE);
        if(elements != null){
        	for(int i=0,size=elements.size();i<size;i++){
        		JsonArray entry = (JsonArray) elements.get(i);
        		map.put(handlers[entry.getInt(0)], handlers[entry.getInt(1)]);
        	}
        }
        return map;
      }
}
