package org.bark.remoting;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HashMapDeserializer implements Deserializer {

	@Override
    public Object readObject(javax.json.JsonObject jsonObj, Object[] handlers, Object obj) throws Exception {
        org.bark.remoting.DeserializerFactory.getInstance().getDeserializer(AbstractMap.class).readObject(jsonObj, handlers, obj);
        HashMap map = (HashMap)obj;
        Object[] entries = jsonObj.get("_table") == javax.json.JsonValue.NULL ? null : (Object[])handlers[jsonObj.getInt("_table")];
        if(entries != null){
            for(Object entry : entries){
            	Map.Entry e = (Entry) entry;
            	map.put(e.getKey(), e.getValue());
            }
        }
        return map;
//        __o._table = jsonObj.get("_table") == javax.json.JsonValue.NULL ? null : (Object[])handlers[jsonObj.getInt("_table")];
//        __o._threshold = jsonObj.getInt("_threshold");
//        __o._loadFactor = (float)jsonObj.getJsonNumber("_loadFactor").doubleValue();
//        return obj;
      }
}
