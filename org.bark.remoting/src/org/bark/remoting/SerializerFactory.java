package org.bark.remoting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public final class SerializerFactory {
	private static final SerializerFactory _INSTANCE = new SerializerFactory();
	private static final Map<Class<?>, Serializer> serializers = new HashMap<>();
	static {
		serializers.put(ArrayList.class, new ArrayListSerializer());
		serializers.put(LinkedList.class, new LinkedListSerializer());
		
		serializers.put(HashMap.class, new HashMapSerializer());
		serializers.put(HashSet.class, new HashSetSerializer());
	}
	private SerializerFactory() {
	}

	public static final SerializerFactory getInstance() {
		return _INSTANCE;
	}
	
	public Serializer getSerializer(Class<?> clazz){
		Serializer result = serializers.get(clazz);
		return result;
	}

	public void register(Class<?> key, Serializer value) {
		serializers.put(key, value);
		
	}
}
