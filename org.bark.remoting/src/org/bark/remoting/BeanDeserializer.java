package org.bark.remoting;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import javax.json.JsonObject;

public class BeanDeserializer implements Deserializer {
	private final Class<?> _type;
	private Field[] _fields;
	
	public BeanDeserializer(Class<?> _type) {
		this._type = _type;
		introspect();
	}

	@SuppressWarnings("unchecked")
	public Object readObject(JsonObject jsonObj, Object[] handlers) {
		Object result = instantiate();

		try {
			for (int i = 0; i < _fields.length; i++) {
				Field field = _fields[i];
				Class<?> type = field.getType();
				if (int.class.equals(type) || byte.class.equals(type)
						|| short.class.equals(type) || int.class.equals(type)) {
					field.set(result, jsonObj.getInt(field.getName()));
				} else if (long.class.equals(type)) {
					field.set(result, jsonObj.getInt(field.getName()));
				} else if (double.class.equals(type)
						|| float.class.equals(type)) {
					field.set(result, jsonObj.getJsonNumber(field.getName()) .doubleValue());
				} else if (boolean.class.equals(type)) {
					field.set(result, jsonObj.getBoolean(field.getName()));
				} else if (String.class.equals(type)) {
					field.set(result, jsonObj.getString(field.getName()));
				} else if (java.util.Date.class.equals(type)) {
					field.set(result, new java.util.Date(jsonObj.getInt(field.getName())));
				} else if (java.sql.Date.class.equals(type)) {
					field.set(result, new java.sql.Date(jsonObj.getInt(field.getName())));
				} else if (java.sql.Timestamp.class.equals(type)) {
					field.set(result, new java.sql.Timestamp(jsonObj.getInt(field.getName())));
				} else if (java.sql.Time.class.equals(type)) {
					field.set(result, new java.sql.Time(jsonObj.getInt(field.getName())));
				} else if (type.isArray()) {
					int refId = jsonObj.getInt(field.getName());
					field.set(result, handlers[refId]);
				} else if (type.isEnum()) {
					field.set(result, Enum.valueOf((Class) field.getType(), jsonObj.getString(field.getName())));
				} else if (type.isAnonymousClass()) {

				} else if (Class.class == type) {
					String className = jsonObj.getString(field.getName());
					field.set(result, Class.forName(className));
				} else {
					int refId = jsonObj.getInt(field.getName());
					field.set(result, handlers[refId]);
				}
			}
		} catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage() + "\n class: "
					+ _type.getName() + " (object=" + jsonObj + ")", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage() + "\n class: "
					+ _type.getName() + " (object=" + jsonObj + ")", e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage() + "\n class: "
					+ _type.getName() + " (object=" + jsonObj + ")", e);
		}

		return result;
	}

	protected Object instantiate() {
		try {
			return _type.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("'" + _type.getName()
					+ "' could not be instantiated", e);
		}
	}

	protected void introspect() {
		Class<?> cl = this._type;
		ArrayList<Field> primitiveFields = new ArrayList<Field>();
		ArrayList<Field> compoundFields = new ArrayList<Field>();

		for (; cl != null; cl = cl.getSuperclass()) {
			Field[] fields = cl.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];

				if (Modifier.isTransient(field.getModifiers())
						|| Modifier.isStatic(field.getModifiers()))
					continue;

				// XXX: could parameterize the handler to only deal with public
				field.setAccessible(true);

				if (field.getType().isPrimitive()
						|| (field.getType().getName().startsWith("java.lang.") && !field
								.getType().equals(Object.class)))
					primitiveFields.add(field);
				else
					compoundFields.add(field);
			}
		}

		ArrayList<Field> fields = new ArrayList<Field>();
		fields.addAll(primitiveFields);
		fields.addAll(compoundFields);

		_fields = new Field[fields.size()];
		fields.toArray(_fields);
	}

}
