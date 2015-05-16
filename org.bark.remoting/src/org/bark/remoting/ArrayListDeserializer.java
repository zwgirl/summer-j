package org.bark.remoting;

import java.util.ArrayList;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class ArrayListDeserializer implements Deserializer {

    @Override
    public Object readObject(JsonObject json, Object[] handlers, Object obj) throws Exception {
//      org.bark.remoting.DeserializerFactory.getInstance().getDeserializer(AbstractArrayList.class).readObject(jsonObj, handlers, obj);
      ArrayList list = (ArrayList)obj;
      JsonArray array = json.getJsonArray(LarkConstants.VALUE);
      if(array != null){
          for(int i=0,size=array.size(); i<size; i++){
        	  list.add(handlers[array.getInt(i)]);
          }
      }

      return list;
    }

}
