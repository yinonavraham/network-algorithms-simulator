package ynn.network.util;

import java.util.HashMap;

public final class AttributeValueSerializerFactory
{
	private static HashMap<Class<?>, Class<? extends AttributeValueSerializer>> _types;
	
	static {
		_types = new HashMap<Class<?>, Class<? extends AttributeValueSerializer>>();
		// Map types to specific attribute value serializers
//		_types.put(String.class,DefaultValueSerializer.class);
//		_types.put(Boolean.class,DefaultValueSerializer.class);
//		_types.put(Integer.class,DefaultValueSerializer.class);
//		_types.put(Float.class,DefaultValueSerializer.class);
//		_types.put(Double.class,DefaultValueSerializer.class);
//		_types.put(Long.class,DefaultValueSerializer.class);
	}
	
	public static AttributeValueSerializer getSerializer(Class<?> cls)
	{
		Class<? extends AttributeValueSerializer> serializerType;
		serializerType = _types.get(cls);
		try
		{
			return serializerType == null ? new DefaultValueSerializer() : serializerType.newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new DefaultValueSerializer();
		}
	}
}
