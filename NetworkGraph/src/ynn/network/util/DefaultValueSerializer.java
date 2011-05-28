package ynn.network.util;

import java.lang.reflect.Method;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DefaultValueSerializer implements AttributeValueSerializer
{

	@Override
	public void serialize(Document document, Element element, Object value)
	{
		if (value != null) 
		{
			String sVal = "" + value;
			element.setTextContent(sVal);
		}
	}

	@Override
	public Object deserialize(Node node, Class<?> cls)
	{
		if (cls == String.class) return node.getTextContent();
		try
		{
			Method method = cls.getMethod("valueOf", String.class);
			return method.invoke(null, node.getTextContent());
		}
		catch (Exception e)
		{
			return null;
		}
	}

}
