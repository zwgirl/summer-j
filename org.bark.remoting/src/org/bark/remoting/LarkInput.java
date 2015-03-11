package org.bark.remoting;

import static org.bark.remoting.LarkConstants.CLASS;
import static org.bark.remoting.LarkConstants.VALUE;
import static org.bark.remoting.LarkConstants.ELEMNTTYPE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
public class LarkInput {

	@SuppressWarnings("unchecked")
	public Object readObject(String data) {
		try (InputStream is = new ByteArrayInputStream(data.getBytes());
				JsonReader rdr = Json.createReader(is)) {
			JsonArray root = rdr.readArray();
			int length = root.size();
			Object[] handlers = new Object[length];
			Class<?>[] clazzs = new Class<?>[length];
			boolean[] flags = new boolean[length];
			for(int i=0; i<length; i++){
				JsonObject element = root.getJsonObject(i);
				String className = element.getString(CLASS);
				Class<?> clazz = Class.forName(className);
				clazzs[i] = clazz;
				if(clazz.isArray()){
					String elementTypeName = element.getString(ELEMNTTYPE);
					Class<?> componentType = Class.forName(elementTypeName);
					JsonArray jsonArray = element.getJsonArray(VALUE);
					handlers[i] = Array.newInstance(componentType, jsonArray.size());
					flags[i] = true;
				}
				if(clazz.equals(int.class) || clazz.equals(byte.class) || clazz.equals(short.class) ||
						clazz.equals(long.class) ){
					handlers[i] = element.getInt(VALUE);
					flags[i] = true;
				} else if(clazz.equals(float.class) || clazz.equals(double.class)){
					handlers[i] = element.getJsonNumber(VALUE).doubleValue();
					flags[i] = true;
				} else if(clazz.equals(Class.class)){
					handlers[i] = Class.forName(element.getString(VALUE));
					flags[i] = true;
				} else if(clazz.isEnum()){
					clazz = Class.forName(className);
					handlers[i] = Enum.valueOf((Class) clazz, element.getString(LarkConstants.VALUE));
					flags[i] = true;
				} else if(clazz.equals(String.class)){
					handlers[i] = element.getJsonNumber(VALUE).doubleValue();
					flags[i] = true;
				} else if(clazz.equals(java.util.Date.class)){
					handlers[i] = new java.util.Date(element.getInt(VALUE));
					flags[i] = true;
				} else if(clazz.equals(java.sql.Date.class)){
					handlers[i] = new java.sql.Date(element.getInt(VALUE));
					flags[i] = true;
				} else if(clazz.equals(java.sql.Time.class)){
					handlers[i] = new java.sql.Time(element.getInt(VALUE));
					flags[i] = true;
				} else if(clazz.equals(java.sql.Timestamp.class)){
					handlers[i] = new java.sql.Timestamp(element.getInt(VALUE));
					flags[i] = true;
				} else {
					handlers[i] = clazz.newInstance();
				}
			}
			
			for(int i=0; i<length; i++){
				if(flags[i]){
					continue;
				}
				Deserializer deser = DeserializerFactory.getInstance().getDeserializer(clazzs[i]);
				deser.readObject(root.getJsonObject(i), handlers);
			}
			
			return handlers[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	

}
