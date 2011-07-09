package ynn.tech.algorithms.network;

import ynn.network.model.Attribute;
import ynn.network.model.Node;

public class SetNodeAttributeCommand implements Command
{
	protected Node _node;
	protected Attribute _attr;
	protected Object _value;
	protected Object _oldValue;
	private boolean _executed;
	protected String _message;
	
	public SetNodeAttributeCommand(Node node, Attribute attribute, Object value)
	{
		this(node,attribute,value,null);
	}
	
	public SetNodeAttributeCommand(Node node, Attribute attribute, Object value, String message)
	{
		_node = node;
		_attr = attribute;
		_value = value;
		_executed = false;
		_message = message;
	}

	@Override
	public final void execute()
	{
		_oldValue = _node.getAttributeValue(_attr);
		onExecute();
		_executed = true;
	}
	
	protected void onExecute()
	{
		_node.putAttribute(_attr, _value);
	}

	@Override
	public final void undo()
	{
		if (canUndo()) 
		{
			onUndo();
			_executed = false;
		}
	}
	
	protected void onUndo()
	{
		_node.putAttribute(_attr, _oldValue);
	}

	@Override
	public boolean canUndo()
	{
		return _executed;
	}

	@Override
	public String toString()
	{  
		String suffix = _message == null ? "" : " - " + _message;
		return String.format("%s: Set %s: %s -> %s%s", _node, _attr, ""+_oldValue, ""+_value, suffix);
	}
}
