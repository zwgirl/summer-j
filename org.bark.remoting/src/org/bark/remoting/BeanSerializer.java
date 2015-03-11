package org.bark.remoting;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import javax.json.JsonObjectBuilder;

public class BeanSerializer implements Serializer {
	private Class<?> clazz;
	private Field[] _fields;

	public BeanSerializer(Class<?> clazz) {
		this.clazz = clazz;
		introspect();
	}

	@Override
	public void writeObject(JsonObjectBuilder builder, Handler handler,
			Object value) {
		try {
			for (int i = 0; i < _fields.length; i++) {
				Field field = _fields[i];
				Class<?> type = field.getType();
				Object propValue = field.get(value);
				if (propValue == null) {
					builder.addNull(field.getName());
					return;
				}

				if (int.class.equals(type) || byte.class.equals(type)
						|| short.class.equals(type) || int.class.equals(type)) {
					builder.add(field.getName(), (int) propValue);
				} else if (long.class.equals(type)) {
					builder.add(field.getName(), (long) propValue);
				} else if (double.class.equals(type)
						|| float.class.equals(type)) {
					builder.add(field.getName(), (double) propValue);
				} else if (boolean.class.equals(type)) {
					builder.add(field.getName(), (boolean) propValue);
				} else if (String.class.equals(type)) {
					builder.add(field.getName(), (String) propValue);
				} else if (Class.class == type) {
					builder.add(field.getName(),
							((Class) propValue).getCanonicalName());
				} else if (java.util.Date.class.equals(type)
						|| java.sql.Date.class.equals(type)
						|| java.sql.Timestamp.class.equals(type)
						|| java.sql.Time.class.equals(type)) {
					builder.add(field.getName(),
							((java.util.Date) propValue).getTime());
				} else if (field.getType().isArray()) {
					int refId = handler.sharedHandler(propValue);
					builder.add(field.getName(), refId);
				} else if (field.getType().isEnum()) {
					Enum item = (Enum) propValue;
					builder.add(field.getName(), item.name());
				} else if (field.getType().isAnonymousClass()) {
					
				} else {
					builder.add(field.getName(), handler.sharedHandler(propValue));
				}
			}
		} catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage() + "\n class: "
					+ value.getClass().getName() + " (object=" + value + ")", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage() + "\n class: "
					+ value.getClass().getName() + " (object=" + value + ")", e);
		} 
	}

	protected void introspect() {
		Class<?> cl = this.clazz;
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
