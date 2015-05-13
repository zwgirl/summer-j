package org.bark.remoting;

import java.util.ArrayList;


public class ArrayListSerializer implements Serializer {

	 @Override
     public void writeObject(javax.json.JsonObjectBuilder builder, org.bark.remoting.ReferenceProcessor handler, Object value) {
//       org.bark.remoting.SerializerFactory.getInstance().getSerializer(ArrayList.class).writeObject(builder, handler, value);
       ArrayList __o = (ArrayList)value;
       builder = __o.isEmpty() ? builder.addNull("elementData") : builder.add("elementData", handler.shared(__o.toArray()));
     }

}
