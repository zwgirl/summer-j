package org.bark.remoting;

import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class Person{
	private String name;
	private int age;
	
	private Person parent;
	
//	Integer[] ia = {1,2,3,4,5};
	
	public Person(){}
	
	public Person(String name, int age, Person parent){
		this.name = name;
		this.age = age;
		this.parent = parent;
	}
	
	static {
		org.bark.remoting.SerializerFactory.getInstance().register(Person.class, new Serializer(){
			@Override
			public void writeObject(javax.json.JsonObjectBuilder builder,
					org.bark.remoting.ReferenceProcessor handler, Object value) {
				builder.add("name", ((Person)value).name);
				builder.add("name", ((Person)value).age);
			}
		});
		org.bark.remoting.DeserializerFactory.getInstance().register(Person.class, new Deserializer(){
			@Override
			public Object readObject(JsonObject jsonObj, Object[] handlers, Object obj) {
				
				Person p = (Person) obj;
				p.name = jsonObj.getString("name");
				p.age = jsonObj.getInt("name");
				
				JsonValue ref = jsonObj.get("parent");
				if(ref == JsonValue.NULL){
					p.parent = null;
				} else {
					p.parent = (Person) handlers[((JsonNumber)ref).intValue()];
				}
				
				return null;
			}
		});
	}
}