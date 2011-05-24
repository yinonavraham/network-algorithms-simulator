package ynn.network.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface AttributeValueSerializer
{
	void serialize(Document document, Element element, Object value);
	Object deserialize(Element element, Class<?> cls);
}
