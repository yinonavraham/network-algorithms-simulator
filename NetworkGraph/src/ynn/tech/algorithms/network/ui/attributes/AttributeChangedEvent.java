package ynn.tech.algorithms.network.ui.attributes;

import ynn.network.model.Node;

public class AttributeChangedEvent
{
	private Node _node;
	private String _attribute;
	private Object _oldValue;
	private Object _newValue;
	
	public AttributeChangedEvent(Node node, String attribute, Object oldValue, Object newValue)
	{
		_node = node;
		_attribute = attribute;
		_oldValue = oldValue;
		_newValue = newValue;
	}
	
	public Node getNode()
	{
		return _node;
	}
	
	public String getAttribute()
	{
		return _attribute;
	}
	
	public Object getOldValue()
	{
		return _oldValue;
	}
	
	public Object getNewValue()
	{
		return _newValue;
	}
	
}
