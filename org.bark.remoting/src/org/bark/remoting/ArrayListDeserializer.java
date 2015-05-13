package org.bark.remoting;

import java.util.ArrayList;

public class ArrayListDeserializer implements Deserializer {

    @Override
    public Object readObject(javax.json.JsonObject jsonObj, Object[] handlers, Object obj) throws Exception {
//      org.bark.remoting.DeserializerFactory.getInstance().getDeserializer(AbstractArrayList.class).readObject(jsonObj, handlers, obj);
      ArrayList __o = (ArrayList)obj;
      Object[] elements = null;
      elements = jsonObj.get("elementData") == javax.json.JsonValue.NULL ? null : (Object[])handlers[jsonObj.getInt("elementData")];
      if(elements != null){
          for(Object element : elements){
        	  __o.add(element);
          }
      }

      return __o;
    }

}
