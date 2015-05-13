package org.bark.remoting;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HashMapSerializer implements Serializer {

	@Override
    public void writeObject(javax.json.JsonObjectBuilder builder, org.bark.remoting.ReferenceProcessor handler, Object value) {
        org.bark.remoting.SerializerFactory.getInstance().getSerializer(AbstractMap.class).writeObject(builder, handler, value);
        HashMap map = (HashMap)value;
        Set<Map.Entry> set = map.entrySet();
        
        builder = set.isEmpty() ? builder.addNull("_table") : builder.add("_table", handler.shared(set.toArray()));
//        builder.add("_loadFactor", map.loadFactor());
//        builder.add("_loadFactor", __o._loadFactor);
      }

}
