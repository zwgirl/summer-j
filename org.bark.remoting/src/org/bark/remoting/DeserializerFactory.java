package org.bark.remoting;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public final class DeserializerFactory {
	private static final DeserializerFactory _INSTANCE = new DeserializerFactory();
	private static final Map<Class<?>, Deserializer> deseserializers = new HashMap<>();
	static {
		deseserializers.put(AbstractCollection.class, new AbstractCollectionDeserializer());
		deseserializers.put(AbstractList.class, new AbstractListDeserializer());
		deseserializers.put(ArrayList.class, new ArrayListDeserializer());
		
		deseserializers.put(HashSet.class, new HashSetDeserializer());
		deseserializers.put(HashMap.class, new HashMapDeserializer());
	}
	private DeserializerFactory() {
	}

	public static final DeserializerFactory getInstance() {
		return _INSTANCE;
	}
	
	public Deserializer getDeserializer(Class<?> clazz){
		Deserializer result = deseserializers.get(clazz);
		return result;
	}

	public synchronized void register(Class<?> key, Deserializer value) {
		deseserializers.put(key, value);
	}
}
