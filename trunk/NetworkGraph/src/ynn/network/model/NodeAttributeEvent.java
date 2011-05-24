package ynn.network.model;

public class NodeAttributeEvent extends AttributeEvent
{
	private Node _node;
	
	public NodeAttributeEvent(Node node, int type, String attribute, Object oldValue, Object newValue)
	{
		super(type, attribute, oldValue, newValue);
		_node = node;
	}
	
	public NodeAttributeEvent(Node node, AttributeEvent e)
	{
		super(e.getType(), e.getAttribute(), e.getOldValue(), e.getNewValue());
		_node = node;
	}
	
	public Node getNode()
	{
		return _node;
	}
}
