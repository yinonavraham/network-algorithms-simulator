package ynn.network.util;

import java.lang.reflect.Method;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DefaultValueSerializer implements AttributeValueSerializer
{

	@Override
	public void serialize(Document document, Element element, Object value)
	{
		if (value != null) 
		{
			String sVal = "" + value;
			element.setNodeValue(sVal);
		}
	}

	@Override
	public Object deserialize(Element element, Class<?> cls)
	{
		if (cls == String.class) return element.getNodeValue();
		try
		{
			Method method = cls.getMethod("valueOf", String.class);
			return method.invoke(null, element.getNodeValue());
		}
		catch (Exception e)
		{
			return null;
		}
	}

}
