package ynn.network.util;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ynn.network.model.Node;
import ynn.network.ui.AbstractShape;
import ynn.network.ui.ConnectorShape;
import ynn.network.ui.ConnectorShape.Direction;
import ynn.network.ui.NetworkView;
import ynn.network.ui.NodeShape;

public class NetworkSerializer
{
	private NetworkView _view;
	private Map<AbstractShape,Integer> _shapesToSerialize;
	private Map<Integer,AbstractShape> _shapesToDeserialize;
	private int _shapeId;
	
	public NetworkSerializer(NetworkView view)
	{
		_view = view;
		_shapesToSerialize = new HashMap<AbstractShape, Integer>();
		_shapesToDeserialize = new HashMap<Integer, AbstractShape>();
		_shapeId = 0;
	}
	
	public void serialize(File file) throws IOException
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element eNetwork = doc.createElement("Network");
			_shapeId = 0;
			_shapesToSerialize.clear();
			serializeNodeShapes(doc, eNetwork);
			serializeConnectorShapes(doc, eNetwork);
			doc.appendChild(eNetwork);
			writeXmlFile(doc, file);
		}
		catch (ParserConfigurationException e)
		{
			throw new IOException(e);
		}
	}

	private void serializeNodeShapes(Document doc, Element eNetwork)
	{
		Element eNodes = doc.createElement("Nodes");
		for (NodeShape node : _view.getNodes())
		{
			serializeNodeShape(doc, eNodes, node);
		}
		eNetwork.appendChild(eNodes);
	}
	
	private void serializeConnectorShapes(Document doc, Element eNetwork)
	{
		Element eConnectors = doc.createElement("Connectors");
		for (ConnectorShape connector : _view.getConnectors())
		{
			serializeConnectorShape(doc, eConnectors, connector);
		}
		eNetwork.appendChild(eConnectors);
	}

	private void serializeConnectorShape(Document doc, Element eConnectors, ConnectorShape connector)
	{
		Element eConnector = doc.createElement("Connector");

		Attr attrDirection = doc.createAttribute("direction");
		attrDirection.setNodeValue(connector.getDirection().name());
		eConnector.setAttributeNode(attrDirection);
		Attr attrVertex1 = doc.createAttribute("vertex1");
		attrVertex1.setNodeValue(String.valueOf(_shapesToSerialize.get(connector.getVertex1())));
		eConnector.setAttributeNode(attrVertex1);
		Attr attrVertex2 = doc.createAttribute("vertex2");
		attrVertex2.setNodeValue(String.valueOf(_shapesToSerialize.get(connector.getVertex2())));
		eConnector.setAttributeNode(attrVertex2);
		
		eConnectors.appendChild(eConnector);
	}

	public static void writeXmlFile(Document doc, File file) {
	    try {
	        // Prepare the DOM document for writing
	        Source source = new DOMSource(doc);

	        // Prepare the output file
	        Result result = new StreamResult(file);

	        // Write the DOM document to the file
	        Transformer xformer = TransformerFactory.newInstance().newTransformer();
	        xformer.transform(source, result);
	    } catch (TransformerConfigurationException e) {
	    } catch (TransformerException e) {
	    }
	}
	
	public void deserialize(File file) throws IOException
	{
		try
		{
			_shapesToDeserialize.clear();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			org.w3c.dom.Node eNetwork = doc.getElementsByTagName("Network").item(0);
			for (int i = 0 ; i < eNetwork.getChildNodes().getLength(); i++)
			{
				org.w3c.dom.Node node = eNetwork.getChildNodes().item(i);
				if (node.getNodeName().equals("Nodes"))
				{
					deserializeNodeShapes(node);
				}
				else if (node.getNodeName().equals("Connectors"))
				{
					deserializeConnectorShapes(node);
				}
			}
		}
		catch (Exception e)
		{
			throw new IOException(e);
		}
	}
	
	private void deserializeNodeShapes(org.w3c.dom.Node eNodes)
	{
		for (int i = 0; i < eNodes.getChildNodes().getLength(); i++)
		{
			deserializeNodeShape(eNodes.getChildNodes().item(i));
		}
	}
	
	private void deserializeConnectorShapes(org.w3c.dom.Node eConnectors)
	{
		for (int i = 0; i < eConnectors.getChildNodes().getLength(); i++)
		{
			deserializeConnectorShape(eConnectors.getChildNodes().item(i));
		}
	}

	private void deserializeNodeShape(org.w3c.dom.Node eNode)
	{
		NodeShape shape = new NodeShape();
		
		org.w3c.dom.Node attrID = eNode.getAttributes().getNamedItem("id");
		Integer id = Integer.valueOf(attrID.getNodeValue());
		org.w3c.dom.Node attrPosition = eNode.getAttributes().getNamedItem("position");
		Point2D position = deserializePoint2D(attrPosition.getNodeValue());
		shape.setPosition(position);
		org.w3c.dom.Node attrText = eNode.getAttributes().getNamedItem("text");
		String text = attrText.getNodeValue();
		shape.setText(text);
		
		_shapesToDeserialize.put(id, shape);
		_view.addShape(shape);
	}

	private void deserializeConnectorShape(org.w3c.dom.Node eConnector)
	{
		ConnectorShape connector = new ConnectorShape();
		
		org.w3c.dom.Node attrDirection = eConnector.getAttributes().getNamedItem("direction");
		Direction direction = Direction.valueOf(attrDirection.getNodeValue());
		connector.setDirection(direction);
		org.w3c.dom.Node attrVertex1 = eConnector.getAttributes().getNamedItem("vertex1");
		Integer id = Integer.valueOf(attrVertex1.getNodeValue());
		connector.setVertex1(_shapesToDeserialize.get(id));
		org.w3c.dom.Node attrVertex2 = eConnector.getAttributes().getNamedItem("vertex2");
		id = Integer.valueOf(attrVertex2.getNodeValue());
		connector.setVertex2(_shapesToDeserialize.get(id));
		
		_view.addShape(connector);
	}

	private void serializeNodeShape(Document doc, Element eNodes, NodeShape node)
	{
		Element eNode = doc.createElement("Node");
		
		int id = _shapeId++;
		_shapesToSerialize.put(node, id);
		Attr attrID = doc.createAttribute("id");
		attrID.setNodeValue(String.valueOf(id));
		eNode.setAttributeNode(attrID);
		Attr attrPosition = doc.createAttribute("position");
		attrPosition.setNodeValue(serializePoint2D(node.getPosition()));
		eNode.setAttributeNode(attrPosition);
		Attr attrText = doc.createAttribute("text");
		attrText.setNodeValue(node.getText());
		eNode.setAttributeNode(attrText);
		serializeNodeData(doc, eNode, (Node)node.getData());
		
		eNodes.appendChild(eNode);
	}
	
	private void serializeNodeData(Document doc, Element eNode, Node data)
	{
		Element eData = doc.createElement("Data");
		 
		for (String attr : data.getAttributes())
		{
			Element eAttr = doc.createElement("Attribute");
			Attr attrName = doc.createAttribute("name");
			attrName.setNodeValue(attr);
			eAttr.setAttributeNode(attrName);
			Object value = data.getAttributeValue(attr);
			if (value != null)
			{
				Attr attrType = doc.createAttribute("type");
				attrType.setNodeValue(value.getClass().getName());
				eAttr.setAttributeNode(attrType);
				AttributeValueSerializer serializer = 
					AttributeValueSerializerFactory.getSerializer(value.getClass());
				serializer.serialize(doc, eAttr, value);
			}
			eData.appendChild(eAttr);
		}
		
		eNode.appendChild(eData);
	}

	private String serializePoint2D(Point2D point)
	{
		return point.getX() + "," + point.getY();
	}
	
	private Point2D deserializePoint2D(String value)
	{
		String[] parts = value.split(",");
		double x = Double.parseDouble(parts[0]);
		double y = Double.parseDouble(parts[1]);
		return new Point2D.Double(x,y);
	}
}
