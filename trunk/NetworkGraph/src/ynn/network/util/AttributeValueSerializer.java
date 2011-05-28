package ynn.network.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface AttributeValueSerializer
{
	void serialize(Document document, Element element, Object value);
	Object deserialize(Node node, Class<?> cls);
}
