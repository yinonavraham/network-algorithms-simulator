package ynn.network.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Node
{
	private List<DirectedNeighbor> _neighbors;
	private Map<String,Object> _attributes;
	private List<INodeListener> _listeners;
	private String _name;
	
	public Node()
	{
		_neighbors = new LinkedList<DirectedNeighbor>();
		_attributes = new HashMap<String, Object>();
		_listeners = new LinkedList<INodeListener>();
		_name = null;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void addNeighbor(Node node)
	{
		if (!_neighbors.contains(node))
		{
			DirectedNeighbor neighbor = new DirectedNeighbor(node, false);
			_neighbors.add(neighbor);
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
		List<Node> neighbors = new ArrayList<Node>(_neighbors.size());
		for (DirectedNeighbor neighbor : _neighbors) neighbors.add(neighbor.getNeighbor());
		return neighbors;
	}
	
	public List<DirectedNeighbor> getDirectedNeighbors()
	{
		return _neighbors;
	}
	
	public void setNeighborDirection(Node node, Direction direction)
	{
		Direction currDirection = getNeighborDirection(node);
		if (currDirection != null && !currDirection.equals(direction))
		{
			int index = _neighbors.indexOf(node);
			if (index >= 0)
			{
				DirectedNeighbor neighbor = _neighbors.get(index);
				switch (direction)
				{
				case Both:
					neighbor.setDirected(true);
					node.setNeighborDirected(this, true);
					break;
				case Default:
					neighbor.setDirected(true);
					node.setNeighborDirected(this, false);
					break;
				case Other:
					neighbor.setDirected(false);
					node.setNeighborDirected(this, true);
					break;
				case None:
					neighbor.setDirected(false);
					node.setNeighborDirected(this, false);
					break;
				}
			}
			fireNeighborDirectionChanged(node);
		}
	}
	
	protected void setNeighborDirected(Node node, boolean directed)
	{
		int index = _neighbors.indexOf(node);
		if (index >= 0)
		{
			DirectedNeighbor neighbor = _neighbors.get(index);
			neighbor.setDirected(directed);
		}
	}
	
	public Direction getNeighborDirection(Node node)
	{
		Direction direction = null;
		int index = _neighbors.indexOf(node);
		if (index >= 0)
		{
			DirectedNeighbor neighbor = _neighbors.get(index);
			boolean directedTo = neighbor.isDirected();
			boolean directedFrom = node.isNeighborDirected(this);
			if (directedTo && directedFrom)
			{
				direction = Direction.Both;
			}
			else if (directedTo)
			{
				direction = Direction.Default;
			}
			else if (directedFrom)
			{
				direction = Direction.Other;
			}
			else direction = Direction.None;
		}
		return direction;
	}
	
	public boolean isNeighborDirected(Node node)
	{
		int index = _neighbors.indexOf(node);
		if (index >= 0)
		{
			DirectedNeighbor neighbor = _neighbors.get(index);
			return neighbor.isDirected();
		}
		return false;
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
	
	private void fireNeighborDirectionChanged(Node neighbor)
	{
		NodeNeighborEvent e = new NodeNeighborEvent(NodeNeighborEvent.DIRECTION_CHANGED, this, neighbor);
		for (INodeListener l : _listeners) l.nodeNeighborsChanged(e);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		else if (obj instanceof DirectedNeighbor)
		{
			return super.equals(((DirectedNeighbor)obj).getNeighbor());
		}
		else if (obj instanceof Node)
		{
			return super.equals((Node)obj);	
		} 
		else return false;
	}
	
	@Override
	public String toString()
	{
		return String.format("Node: %s", _name);
	}
}
