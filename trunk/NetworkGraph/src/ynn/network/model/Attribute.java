package ynn.network.model;

public class Attribute
{
	private String _name;
	private Class<?> _type;
	
	public Attribute(String name, Class<?> type)
	{
		_name = name;
		_type = type;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public Class<?> getType()
	{
		return _type;
	}
	
	@Override
	public String toString()
	{
		return _name;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		else if (obj instanceof Attribute)
		{
			return _name.equals(((Attribute)obj).getName());
		}
		else if (obj instanceof String)
		{
			return _name.equals(obj);
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return _name.hashCode();
	}
}
