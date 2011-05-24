package ynn.network.model;

public class AttributeEvent
{
	public static final int ADDED = 0;
	public static final int REMOVED = 1;
	public static final int CHANGED = 2;
	
	private int _type;
	private String _attribute;
	private Object _oldValue;
	private Object _newValue;
	
	public AttributeEvent(int type, String attribute, Object oldValue, Object newValue)
	{
		_type = type;
		_attribute = attribute;
		_oldValue = oldValue;
		_newValue = newValue;
	}
	
	public int getType()
	{
		return _type;
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
