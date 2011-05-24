package ynn.network.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Node
{
	private List<Node> _neighbors;
	private Map<String,Object> _attributes;
	private List<INodeListener> _listeners;
	
	public Node()
	{
		_neighbors = new LinkedList<Node>();
		_attributes = new HashMap<String, Object>();
		_listeners = new LinkedList<INodeListener>();
	}
	
	public void addNeighbor(Node node)
	{
		if (!_neighbors.contains(node))
		{
			_neighbors.add(node);
			node.addNeighbor(this);
			fireNeighborAdded(node);
		}
	}
	
	public void removeNeighbor(Node node)
	{
		if (_neighbors.contains(node))
		{
			_neighbors.remove(node);
			node.removeNeighbor(this);
			fireNeighborRemoved(node);
		}
	}
	
	public List<Node> getNeighbors()
	{
		return _neighbors;
	}
	
	public boolean isNeighborOf(Node node)
	{
		return _neighbors.contains(node);
	}
	
	public void putAttribute(String attribute, Object value)
	{
		if (_attributes.containsKey(attribute))
		{
			Object oldValue = _attributes.get(attribute);
			_attributes.put(attribute, value);
			fireAttributeChanged(attribute, oldValue, value);
		}
		else
		{
			_attributes.put(attribute, value);
			fireAttributeAdded(attribute, value);
		}
	}
	
	public Object getAttributeValue(String attribute)
	{
		return _attributes.get(attribute);
	}
	
	public void removeAttribute(String attribute)
	{
		if (_attributes.containsKey(attribute))
		{
			Object value = _attributes.get(attribute);
			_attributes.remove(attribute);
			fireAttributeRemoved(attribute, value);
		}
	}
	
	public Set<String> getAttributes()
	{
		return _attributes.keySet();
	}
	
	public void addListener(INodeListener l)
	{
		if (!_listeners.contains(l)) _listeners.add(l);
	}
	
	public void removeListener(INodeListener l)
	{
		if (_listeners.contains(l)) _listeners.remove(l);
	}
	
	private void fireAttributeChanged(String attr, Object oldValue, Object newValue)
	{
		NodeAttributeEvent e = new NodeAttributeEvent(this, AttributeEvent.CHANGED, attr, oldValue, newValue);
		for (INodeListener l : _listeners) l.nodeAttributeChanged(e);
	}
	
	private void fireAttributeAdded(String attr, Object value)
	{
		NodeAttributeEvent e = new NodeAttributeEvent(this, AttributeEvent.ADDED, attr, value, value);
		for (INodeListener l : _listeners) l.nodeAttributeChanged(e);
	}
	
	private void fireAttributeRemoved(String attr, Object value)
	{
		NodeAttributeEvent e = new NodeAttributeEvent(this, AttributeEvent.REMOVED, attr, value, value);
		for (INodeListener l : _listeners) l.nodeAttributeChanged(e);
	}
	
	private void fireNeighborAdded(Node neighbor)
	{
		NodeNeighborEvent e = new NodeNeighborEvent(NodeNeighborEvent.ADDED, this, neighbor);
		for (INodeListener l : _listeners) l.nodeNeighborsChanged(e);
	}
	
	private void fireNeighborRemoved(Node neighbor)
	{
		NodeNeighborEvent e = new NodeNeighborEvent(NodeNeighborEvent.REMOVED, this, neighbor);
		for (INodeListener l : _listeners) l.nodeNeighborsChanged(e);
	}
}
